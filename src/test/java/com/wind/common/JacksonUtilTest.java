package com.wind.common;


import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JacksonUtilTest {

    @Test
    public void test() {
        String json = FileUtil.getJsonStr(new File("F:/IdeaProjects/JsonValueTool/src/main/resources/test2.json"));
        Map<String, Object> o0 = JacksonUtil.fetchValue(json, "aggregations.2");
        List<Object> o1 = JacksonUtil.fetchValue(json, "aggregations.2.buckets");
        List<Object> o2 = JacksonUtil.fetchValue(json, "aggregations.2.buckets.3");
        List<Object> o3 = JacksonUtil.fetchValue(json, "aggregations.2.buckets.3.buckets");
        List<Object> o4 = JacksonUtil.fetchValue(json, "aggregations.2.buckets.3.buckets.4");

        System.out.println(o0);
        System.out.println(o1);
        System.out.println(o2);
        System.out.println(o3);
        System.out.println(o4);
    }

}