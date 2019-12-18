package com.suntech.feo.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.utils
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:23
 * ------------    --------------    ---------------------------------
 */
public class JSONUtils {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);
    /**
     * 将一个对象json化
     *
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        return objectToJson(obj,false);
    }
    /**
     * 对象格式化，支持格式化输出
     * @param obj
     * @param pretty
     * @return
     */
    public static String objectToJson(Object obj,boolean pretty) {
        ObjectMapper om = new ObjectMapper();
        String result = null;
        try {
            SimpleFilterProvider fProvider = new SimpleFilterProvider();
            fProvider.setFailOnUnknownId(false);
            om.setFilters(fProvider);
            if(pretty) {
                result = om.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }else{
                result = om.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            logger.error("Json处理异常",e);
        }
        return result;
    }

    /**
     * 将json 转换为指定类型对象集合
     * @param json
     * @param clazz
     * @return
     */
    public  static <T> List<T> jsonToList(String json, Class<T> clazz){
        ObjectMapper om = new ObjectMapper();

        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType javaType = om.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
//		JavaType javaType = getCollectionType(mapper, ArrayList.class, getGenericsType());
        List<T> result = null;
        try {
            result = om.readValue(json,javaType);
        } catch (IOException e) {
            logger.error("jsonToList 处理异常",e);
        }
        return result;
    }

    /**
     * 串行化指定对象的指定字段
     * 指定对象必须实现JSONUtilsSerialization接口
     * @param o
     * @param fields
     * @return
     * @throws JsonProcessingException
     */
    public static String objectToJsonInclude(Object o, String... fields)
    {
        if(fields.length <= 0){
            return null;
        }
        SimpleFilterProvider fProvider = new SimpleFilterProvider();
        //object must implements JSONUtilsSerialization interface
        fProvider.addFilter("fieldFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilters(fProvider);
        String json = null;
        try {
            json = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error("Json处理异常",e);
        }
        return json;
    }

    /**
     * 对象串行化，但是排除序列化fields参数中指定的字段
     *  指定对象必须实现JSONUtilsSerialization接口
     * @param o
     * @param fields
     * @return
     */
    public static String objectToJsonExclude(Object o, String... fields)
    {
        ObjectMapper mapper = new ObjectMapper();
        // Exclude Null Fields
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        FilterProvider fProvider = new SimpleFilterProvider().addFilter(
                "fieldFilter",
                SimpleBeanPropertyFilter.serializeAllExcept(fields));
        mapper.setFilters(fProvider);
        String json = null;
        try {
            json = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error("Json处理异常",e);
        }
        return json;
    }

    /**
     * 从json串构建一个指定类型的类对象实例
     * @param json
     * @param valueType
     * @return
     */
    public static <T> T fromJson(String json,Class<T> valueType){
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        T obj = null;
        try {
            //针对json不认识的字段忽略掉，不要报错
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            obj = om.readValue(json, valueType);
        } catch (JsonParseException e) {
            logger.error("Json解析异常",e);
        } catch (JsonMappingException e) {
            logger.error("JsonMapping异常",e);
        } catch (IOException e) {
            logger.error("IO异常",e);
        }
        return obj;
    }

    /**
     * Map转JSON
     * @param map
     * @return
     */
    public static String map2json(Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        try {
            json = mapper.writeValueAsString(map);
        } catch (JsonGenerationException e) {
            logger.error("Json转换异常",e);
        } catch (JsonMappingException e) {
            logger.error("JsonMapping转换异常",e);
        } catch (IOException e) {
            logger.error("IO异常",e);
        }

        return json;
    }

    /**
     * 获取指定json串中的某一个属性的json子串
     * if whole json is {a:1,person:{id:'111'}}
     * but just need person sub json string  {id:'111'}  , u can using the method:
     * getFieldJsonFromJson(json,'person')
     * fieldName支持子串的子串  比如session.person可以获取到{a:1,session:{id:'',person:{id:''}}
     * @param json
     * @param fieldName
     * @return
     * 		sub json
     */
    public static String getFieldJsonFromJson(String json,String fieldName){
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            JsonNode actualObj = mapper.readValue(json, JsonNode.class);
            JsonNode jn = null;
            if(fieldName.indexOf(".") >= 0){
                String[] fieldNames = fieldName.split("\\.");
                JsonNode iteratorJsonNode = actualObj;
                for(int i=0;i<fieldNames.length;i++){
                    iteratorJsonNode = iteratorJsonNode.get(fieldNames[i]);
                    if(iteratorJsonNode == null) {
                        break;
                    }
                }
                jn = iteratorJsonNode;
            }else{
                jn = actualObj.get(fieldName);
            }
            if(jn != null){
                result = jn.asText();
            }
        } catch (IOException e) {
            logger.error("IO异常",e);
        }
        return result;
    }

    /**
     * 获取指定json的某一个属性节点对象
     * @param json
     * @param fieldName
     * @return
     */
    public static JsonNode getFieldJsonNodeFromJson(String json, String fieldName){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = null;
        try {
            JsonNode actualObj = mapper.readValue(json, JsonNode.class);
            JsonNode jn = null;
            if(fieldName.indexOf(".") >= 0){
                String[] fieldNames = fieldName.split("\\.");
                JsonNode iteratorJsonNode = actualObj;
                for(int i=0;i<fieldNames.length;i++){
                    iteratorJsonNode = iteratorJsonNode.get(fieldNames[i]);
                    if(iteratorJsonNode == null) {
                        break;
                    }
                }
                jn = iteratorJsonNode;
            }else{
                jn = actualObj.get(fieldName);
            }
            if(jn != null){
                result = jn;
            }
        } catch (IOException e) {
            logger.error("IO异常",e);
        }
        return result;
    }


    /**
     * 获取json中相同key的valList
     * @param json
     * @param fieldName
     * @return
     */
    public static List<JsonNode> getFieldValListFromJson(String json, String fieldName){
        ObjectMapper mapper = new ObjectMapper();
        List<JsonNode> result = new ArrayList<>();
        try {
            JsonNode actualObj = mapper.readValue(json, JsonNode.class);
            Iterator<JsonNode> iterator = actualObj.iterator();
            while(iterator.hasNext()){
                JsonNode next = iterator.next();
                JsonNode fieldVal = next.get(fieldName);
                result.add(fieldVal);
            }
        } catch (IOException e) {
            logger.error("IO异常",e);
        }
        return result;

    }



    public static void main(String[] args) {
        String str = "{\"code\":200,\"msg\":\"成功\",\"data\":\"创建成功\"}";

        String code = JSONUtils.getFieldJsonFromJson(str, "code");

        System.out.println(code);


    }
}
