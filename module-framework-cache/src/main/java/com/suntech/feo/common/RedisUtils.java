package com.suntech.feo.common;

import com.suntech.feo.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.common
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 13:04
 * ------------    --------------    ---------------------------------
 */
@Component
public class RedisUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    private static final ConcurrentHashMap<String, Future<String>> lua_script_cache = new ConcurrentHashMap<>();

    @Autowired
    private RedisTemplate redisTemplate;

    private static String redisCode = "utf-8";

    @PostConstruct
    public void init(){
        RedisSerializer stringSerializer=new StringRedisSerializer();
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setKeySerializer(stringSerializer);
    }



    /**
     * 加锁
     * @param key 被秒杀商品的id
     * @param expire 当前线程操作时的 System.currentTimeMillis() + 2000，2000是超时时间，这个地方不需要去设置redis的expire，
     *              也不需要超时后手动去删除该key，因为会存在并发的线程都会去删除，造成上一个锁失效，结果都获得锁去执行，并发操作失败了就。
     * @return
     */
    public boolean lock(String key, Long expire) {
        logger.debug("ready to get redis lock : {}",key);

        boolean result = redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(expire+System.currentTimeMillis()));
        if(result){
            return true;
        }
        //获取key的值，判断是是否超时
        String curVal = (String) redisTemplate.opsForValue().get(key);
        logger.debug("get lock value : {} => {}",key,curVal);
        if (StringUtil.isNotEmpty(curVal) && Long.parseLong(curVal) < System.currentTimeMillis()) {

            //获得之前的key值，同时设置当前的传入的value。这个地方可能几个线程同时过来，但是redis本身天然是单线程的，所以getAndSet方法还是会安全执行，
            //首先执行的线程，此时curVal当然和oldVal值相等，因为就是同一个值，之后该线程set了自己的value，后面的线程就取不到锁了
            String oldVal = (String)redisTemplate.opsForValue().getAndSet(key, String.valueOf(expire+System.currentTimeMillis()));
            if(StringUtil.isNotEmpty(oldVal) && oldVal.equals(curVal)) {
                return true;
            }
        }
        logger.debug("get lock failed : {} => {}",key,expire);
        return false;
    }

    /**
     * 解锁
     * @param key
     * @param expire
     */
    public void unlock(String key, Long expire) {
        try {
            logger.debug("unlock the redis lock : {}",key);
            String curValue = (String)redisTemplate.opsForValue().get(key);
            if (StringUtil.isNotEmpty(curValue) && curValue.equals(String.valueOf(expire))) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            logger.error("unlock redis lock failed:"+key,e);
        }
    }



    private String getSha1Promise(RedisConnection jedis, String script){
        String sha1Id = null;
        Future<String> f = lua_script_cache.get(script);
        if(f == null){
            FutureTask<String> task = new FutureTask<>(new Callable<String>(){
                @Override
                public String call() throws Exception {
                    logger.info("load redis script:\n{}",script);
                    return jedis.scriptLoad(script.getBytes());
                }
            });
            Future<String> f1 = lua_script_cache.putIfAbsent(script, task);
            if(f1 == null){
                task.run();
                f = task;
            }else{
                f = f1;
            }
        }
        try {
            sha1Id = f.get();
        } catch (Throwable e) {
            logger.info("获取redis script sha1失败",e);
        }
        return sha1Id;
    }

    private Object eval(final RedisConnection jedis, final String script,final int keyCount,final String... params){
        long start = 0;
        if(logger.isDebugEnabled()){
            start = System.currentTimeMillis();
        }
        Object obj = null;
        String sha1Id = getSha1Promise(jedis, script);
        if(sha1Id != null){
            //<T> T evalSha(String scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs);
            obj = jedis.evalSha(sha1Id, ReturnType.VALUE,keyCount, toByteArray(params));


        }else{
            obj = jedis.eval(script.getBytes(), ReturnType.VALUE,keyCount,toByteArray(params));
        }
        if(logger.isDebugEnabled()){
            logger.info("执行redis script耗时={}",(System.currentTimeMillis() - start));
        }
        return obj;
    }

    private byte[][] toByteArray(String ... params) {
        byte[][] x = new byte[params.length][];
        for(int i=0;i<x.length;i++){
            x[i] = params[i].getBytes();
        }
        return x;
    }

    public Object eval(final String script,final int keyCount,final String... params){
        return redisTemplate.execute((RedisCallback) connection -> eval(connection,script,keyCount,params));
    }

    public Object eval(final String script){
        return redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return eval(connection,script,0);
            }
        });
    }

    /**
     * 执行设置值，如果由于并发导致设置标记位导致设置失败，丢出TransactionExecFailedException异常
     * @param key
     * @param value
     * @throws TransactionExecFailedException
     */
    public void setTransactionFlag(final String key, final String value,final long expire)
            throws TransactionExecFailedException {
        boolean result = (boolean) redisTemplate
                .execute((RedisCallback) connection -> {
                    if(logger.isDebugEnabled()){
                        logger.debug("ready to set nx:"+key+">>>>"+ value);
                    }
                    boolean ret = connection.setNX(key.getBytes(), value.getBytes());
                    if(ret){//防止没获取到锁也能刷新锁的过期时间
                        //默认缓存2天
                        connection.expire(key.getBytes(), expire);
                    }
                    if(logger.isDebugEnabled()){
                        logger.debug("set nx result:"+ret);
                    }
                    return ret;
                });
        //如果结果为空表示设置失败了
        if(result == false)
            throw new TransactionExecFailedException();
    }

    /**
     * @param keys
     */

    public long del(final String... keys) {
        return (long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param seconds
     */
    public void set(final byte[] key, final byte[] value, final long seconds) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                connection.set(key, value);
                if (seconds > 0) {
                    connection.expire(key, seconds);
                }
                return 1L;
            }
        });
    }

    /**
     * @param key
     * @param seconds
     */
    public void expire(final byte[] key,final long seconds) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                if (seconds > 0) {
                    connection.expire(key, seconds);
                }
                return 1L;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param seconds  过期时间单位为秒
     */
    public void set(String key, String value, long seconds) {
        this.set(key.getBytes(), value.getBytes(), seconds);
    }

    /**
     * @param key
     * @param seconds  过期时间单位为秒
     */
    public void expire(String key, long seconds) {
        this.expire(key.getBytes(), seconds);
    }

    /**
     * @param key
     * @param value
     */
    public void set(String key, String value) {

        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @return
     */
    public String get(final String key) {
        String result = (String) redisTemplate.execute((RedisCallback) connection -> {
            try {
                byte[] obj = connection.get(key.getBytes());
                if(obj != null){
                    return new String(obj, redisCode);
                }else{
                    return null;
                }
            } catch (Exception e){
                logger.error("get cache exception key is:"+key, e);
            }
            return null;
        });
        logger.debug("get cache value:{}=>{}",key, StringUtil.trimLog(result));
        return result;
    }

    public String getAndSet(final String key,final String value) {
        String result = (String) redisTemplate.boundValueOps(key).getAndSet(value);
        return result;
    }

    public String getAndSet(final String key,final String value,long expires) {
        String result = (String) redisTemplate.boundValueOps(key).getAndSet(value);
        redisTemplate.expire(key,expires, TimeUnit.SECONDS);
        return result;
    }

    /**
     * @param pattern
     * @return
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);

    }

    /**
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        logger.debug("【REDIS】redis exist :{}",key);
        Object result =  redisTemplate.execute((RedisCallback) connection -> {
            logger.debug("【REDIS】 redis exist  conn:{}, key:{}",connection,key);
            return connection.exists(key.getBytes());
        });

        logger.debug("【REDIS】 redis exist result: {} -> {}",key,result);

        if(null == result){
            result = false;
        }

        return (boolean) result;
    }

    /**
     * @return
     */
    public String flushDB() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    /**
     * @return
     */
    public long dbSize() {
        return (long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    /**
     * @return
     */
    public String ping() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {

                return connection.ping();
            }
        });
    }

    /**
     * 执行设置值，如果由于并发导致设置标记位导致设置失败，丢出TransactionExecFailedException异常
     * @param key
     * @param value
     * @throws TransactionExecFailedException
     */
    public void setTransactionFlag(final String key, final String value)
            throws TransactionExecFailedException {
        this.setTransactionFlag(key,value,48*60*60);
    }


    /**
     * 针对redis incr命令的封装，实现指定key的值自增长
     * @param key
     * 	key值
     * @param
     * @return
     *  自增长后的值
     */
    public long incr(final String key, Long expire) {
        long result = (long) redisTemplate
                .execute(new RedisCallback() {
                    @Override
                    public Object doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        long result = connection.incr(key.getBytes());
                        if(expire != null){
                            connection.expire(key.getBytes(),expire);
                        }
                        return result;
                    }
                });
        return result;
    }

    /**
     * 不带过期时间的自增长
     * @param key
     * @return
     */
    public long incr(final String key) {
        return incr(key,null);
    }

    /**
     * 针对redis INCRBY，实现指定key的值的增长
     * @param key 	key值
     * @param incr 增长的值
     * @return
     *  自增长后的值
     */
    public long incrBy(final String key,Long incr) {
        long result = (long) redisTemplate
                .execute(new RedisCallback() {
                    @Override
                    public Object doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        return connection.incrBy(key.getBytes(),incr);
                    }

                });
        return result;
    }

    /**
     * 获取缓存的值，之后迅速删除掉
     * @param key
     * 	缓存key
     * @return
     * 	返回指定key对应的值
     */
    public String getAndRemove(final String key) {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    byte[] obj = connection.get(key.getBytes());
                    if(obj != null){
                        connection.del(key.getBytes());
                        return new String(obj, redisCode);
                    }else{
                        return null;
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.error("不支持的编码转换",e);
                }
                return null;
            }
        });
    }

    /**
     *
     * @param key
     * @param value
     */
    public void zrem(final String key, final String value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    public void zrem(String key, Object... array) {
        redisTemplate.opsForZSet().remove(key, array);

    }

    public long zsize(String key){
        return redisTemplate.opsForZSet().size(key);
    }

    public Set zReverseRange(final String key, final long start, final long end){
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    public Set zRange(final String key, final long start, final long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public Set zRangeScore(final String key, final long min, final long max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    public void zadd(final String key,final String value,double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public Double zScore(final String key,final String value) {
        return redisTemplate.opsForZSet().score(key,value);
    }

    public Long zCount(final String key,final double score1,double score2) {
        return redisTemplate.opsForZSet().count(key,score1,score2);
    }

    public void sremove(final String key,final String... value){
        redisTemplate.opsForSet().remove(key,value);
    }

    public void sadd(final String key, final Object ... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public Set smembers(final String key) {
        Set set = redisTemplate.opsForSet().members(key);
        return set;
    }

    public boolean sismember(final String key,final String value){
        return redisTemplate.opsForSet().isMember(key,value);
    }

    public long ssize(final String key){
        return redisTemplate.opsForSet().size(key);
    }

    public Map hgetAll(final String key){
        return redisTemplate.opsForHash().entries(key);
    }

    public Object hget(final String key,final String field){
        return redisTemplate.opsForHash().get(key,field);
    }

    public void hputAll(final String key,final Map<String,String> map){
        redisTemplate.opsForHash().putAll(key,map);
    }

    public void hput(final String key,final String field,final String value){
        redisTemplate.opsForHash().put(key,field,value);
    }

    public boolean hputIfAbsent(final String key,final String field,final String value){
        return redisTemplate.opsForHash().putIfAbsent(key,field,value);
    }

    public void hdel(final String key,final String... fields){
        redisTemplate.opsForHash().delete(key,fields);
    }

    public BoundHashOperations getHashOps(String key){
        return redisTemplate.boundHashOps(key);
    }


    @SuppressWarnings("unchecked")
    public Cursor<String> scan(String pattern, int limit) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        return (Cursor) redisTemplate.executeWithStickyConnection(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize);
            }
        });
    }
}
