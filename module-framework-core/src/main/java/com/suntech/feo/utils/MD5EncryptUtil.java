package com.suntech.feo.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.utils
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 13:46
 * ------------    --------------    ---------------------------------
 */
public class MD5EncryptUtil {
    private static final String ENCODE = "UTF-8";

    /**
     * 功能：MD5加密
     * @param strSrc 加密的源字符串
     * @return 加密串 长度32位(hex串)
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getMessageDigest(String strSrc) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        String strDes = null;
        final String ALGO_MD5 = "MD5";

        byte[] bt = strSrc.getBytes(ENCODE);
        md = MessageDigest.getInstance(ALGO_MD5);
        md.update(bt);
        strDes = StringUtil.byte2hex(md.digest());
        return strDes;
    }

    /**
     * 将字节数组转为HEX字符串(16进制串)
     * @param bts 要转换的字节数组
     * @return 转换后的HEX串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }


    /**
     * 构建签名原文
     *
     * @param signFields
     *            参数列表
     * @param param
     *            参数与值的jsonbject
     * @return
     */
    public static String orgSignSrc(String[] signFields, JSONObject param) {
        if (signFields != null) {
            Arrays.sort(signFields); // 对key按照 字典顺序排序
        }

        StringBuffer signSrc = new StringBuffer("");
        int i = 0;
        for (String field : signFields) {
            signSrc.append(field);
            signSrc.append("=");
            signSrc.append((StringUtil.isEmpty(param.getString(field)) ? ""
                    : param.getString(field)));
            // 最后一个元素后面不加&
            if (i < (signFields.length - 1)) {
                signSrc.append("&");
            }
            i++;
        }
        return signSrc.toString();
    }
}
