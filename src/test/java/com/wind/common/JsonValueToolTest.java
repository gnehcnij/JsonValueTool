package com.wind.common;

import org.junit.Test;

import java.io.File;

public class JsonValueToolTest {

    @Test
    public void test() {
        String json = FileUtil.getJsonStr(new File("F:/IdeaProjects/JsonValueTool/src/main/resources/test.json"));
        Object father = JsonValueTool.getValueByKeyExpression(json, "father", "father#properties#country#name");
        System.out.println(father);
    }

}