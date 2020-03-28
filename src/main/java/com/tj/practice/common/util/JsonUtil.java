package com.tj.practice.common.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/14 18:07
 * JSON与POJO转换工具类.
 */
@Slf4j
public class JsonUtil {
    @SuppressWarnings("unchecked")
    public static <U> U json2object(String json, TypeToken<U> typeToken) {
        Gson gson = new Gson();
        return (U) gson.fromJson(json, typeToken.getType());
    }

    public static <U> U json2object_gson(String json, Class<U> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public static <U> U json2object(String json, TypeReference<U> typeToken){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return (U)  mapper.readValue(json, typeToken);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }



    /**
     *
     * java对象转为json对象
     *
     */
    public static String object2json(Object obj) {
        Gson gson = /*new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();*/new Gson();
        return gson.toJson(obj);
    }

    public static String object2jackJson(Object arg0){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(arg0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T parse(String json, Class<T> clazz) {
        if (json == null || json.length() == 0) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * <p>Description:[json字符串转换为bean，json中是小写下划线格式，bean中是驼峰式]</p>
     * @param json
     * @param
     * @return
     */
    public static <T> T parse2(String json, TypeReference<T> valueTypeRef){
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE);
            return objectMapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fastJsonParse(String json, Class<T> clazz){
        if (json == null || json.length() == 0) {
            return null;
        }
        return JSON.parseObject(json,clazz);
    }

    public static String fastJsonToString(Object obj){
        if(obj == null){
            return null;
        }
        return JSON.toJSONString(obj);
    }

    /**
     *
     * <p>Description:[使用jackson jar包，将json格式的字符串转换为JsonNode,便于后续使用jackson的相关方法进一步解析]</p>
     * @param str
     * @return
     */
    public static JsonNode stringToJsonNode(String str){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(str);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
            return null;
        }
    }
}
