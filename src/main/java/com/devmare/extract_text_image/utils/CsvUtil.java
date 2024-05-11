package com.devmare.extract_text_image.utils;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CsvUtil {

    public static MultipartFile convertCsvToMultipartFile(String csvData, String fileName) {
        try {
            InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
            return new MockMultipartFile("file", fileName, MediaType.TEXT_PLAIN_VALUE, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
