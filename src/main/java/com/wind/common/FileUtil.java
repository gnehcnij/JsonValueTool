package com.wind.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class FileUtil {

    private FileUtil() {
    }

    public static String getJsonStr(File file) {
        ByteArrayOutputStream outputStream = null;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return outputStream == null ? "" : outputStream.toString();
    }

}
