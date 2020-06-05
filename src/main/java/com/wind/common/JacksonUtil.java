package com.wind.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JacksonUtil {

    public static final String DATE_TIME_LONG_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        // 设置在反序列化时忽略在JSON字符串中存在，而在Java中不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 设置Jackson序列化时只包含不为空的字段
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 反序列化的时候，如果遇到空字符串，那么就当作null来处理
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        // 允许key没有引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许key用单引号，不强制双引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 添加日期转换格式
        mapper.setDateFormat(new SimpleDateFormat(DATE_TIME_LONG_FORMAT));
        // 科学计数法转成非科学计数法
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    public static <T> T anyToBean(Object obj, Class<T> clazz) {
        return mapper.convertValue(obj, clazz);
    }

    public static String beanToJson(Object bean) {
        try {
            return mapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("对象转JSON字符串失败");
        }
    }

    /**
     * bean 转 map
     *
     * @param bean 对象
     * @return Map<String, Object>
     */
    public static Map<String, Object> beanToMap(Object bean) {
        return mapper.convertValue(bean, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * bean 转 List
     */
    public static List<Object> beanToList(Object bean) {
        return mapper.convertValue(bean, new TypeReference<List<Object>>() {
        });
    }

    /**
     * bean 转 List
     */
    public static <T> List<T> beanToList(Object bean, TypeReference<List<T>> typeReference) {
        return mapper.convertValue(bean, typeReference);
    }

    public static <T> T jsonToBean(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(json);
        }
    }

    public static <T> T jsonToBean(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new IllegalArgumentException(json);
        }
    }

    public static Map<String, Object> jsonToMap(String json) {
        if (json == null) {
            return null;
        }
        return jsonToBean(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static <T> Map<String, T> jsonToMap(String json, TypeReference<Map<String, T>> typeReference) {
        if (json == null) {
            return null;
        }
        return jsonToBean(json, typeReference);
    }

    public static <K, V> Map<K, V> jsonToMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            throw new IllegalArgumentException(json);
        }
    }

    public static List<Object> jsonToList(String json) {
        return jsonToBean(json, new TypeReference<List<Object>>() {
        });
    }

    public static <T> List<T> jsonToList(String json, Class<T> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            throw new IllegalArgumentException(json);
        }
    }

    /**
     * 校验传进来的字符串是否为json格式
     */
    public static boolean isJSONValid(String jsonInString) {
        if (StringUtil.isBlank(jsonInString)) {
            return false;
        }
        try {
            JsonNode jsonNode = mapper.readTree(jsonInString);
            return jsonNode != null && jsonNode.size() > 0;
        } catch (IOException e) {
            jsonInString = jsonInString.replaceAll("'", "\"");
            try {
                mapper.readTree(jsonInString);
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
    }

    @SuppressWarnings(value = "unchecked")
    public static <T> T fetchValue(String json, String path) {
        if (StringUtil.isBlank(json) || StringUtil.isBlank(path)) {
            return null;
        }
        return (T) doFetchValue(jsonToMap(json), path.split("\\."), 0);
    }

    @SuppressWarnings(value = "unchecked")
    private static Object doFetchValue(Object object, String[] split, int start) {
        String key = split[start];
        if (StringUtil.isBlank(key)) {
            return null;
        }
        if (object instanceof Map) {
            Map<String, Object> objectMap = ((Map<String, Object>) object);
            return doFetchValueFromMap(objectMap, split, start);
        }
        if (object instanceof List) {
            List<Object> objectList = ((List<Object>) object);
            return doFetchValueFromList(objectList, split, start);
        }
        return null;
    }

    private static Object doFetchValueFromMap(Map<String, Object> objectMap, String[] split, int start) {
        if (split.length == start + 1) {
            return objectMap.get(split[start]);
        }
        return doFetchValue(objectMap.get(split[start]), split, start + 1);
    }

    private static Object doFetchValueFromList(List<Object> objectList, String[] split, int start) {
        List<Object> list = new ArrayList<>();
        for (Object o : objectList) {
            list.add(doFetchValue(o, split, start));
        }
        return list;
    }

}
