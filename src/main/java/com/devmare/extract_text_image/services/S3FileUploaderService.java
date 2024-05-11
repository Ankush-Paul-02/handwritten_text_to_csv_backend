package com.devmare.extract_text_image.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3FileUploaderService {

    String uploadImage(MultipartFile file);

    List<String> allFiles();

    String preSignedUrl(String fileName);
}
