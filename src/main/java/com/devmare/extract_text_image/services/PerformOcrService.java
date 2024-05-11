package com.devmare.extract_text_image.services;

import org.springframework.web.multipart.MultipartFile;

public interface PerformOcrService {
    String performOCR(MultipartFile file, String outputFilePath);
}
