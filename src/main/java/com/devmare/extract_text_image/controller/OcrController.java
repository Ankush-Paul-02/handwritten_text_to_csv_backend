package com.devmare.extract_text_image.controller;

import com.devmare.extract_text_image.models.FileModel;
import com.devmare.extract_text_image.services.FileService;
import com.devmare.extract_text_image.services.PerformOcrService;
import com.devmare.extract_text_image.services.S3FileUploaderService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@RestController
@RequestMapping("api/v1/ocr")
public class OcrController {

    private final PerformOcrService performOcr;
    private final S3FileUploaderService s3FileUploaderService;
    private final FileService fileService;

    @CrossOrigin
    @PostMapping("perform")
    public ResponseEntity<String> performOCR(
            @RequestParam("file") MultipartFile file
    ) {
        String fileUrl = performOcr.performOCR(file, "D:\\scanned_document.csv");
        return ResponseEntity.ok(fileUrl);
    }

    @CrossOrigin
    @GetMapping("s3/file/all")
    public ResponseEntity<List<String>> getS3FileUrls() {
        List<String> fileUrls = s3FileUploaderService.allFiles();
        return ResponseEntity.ok(fileUrls);
    }

    @CrossOrigin
    @GetMapping("file/all")
    public ResponseEntity<List<FileModel>> getAllFiles() {
        List<FileModel> fileModelList = fileService.getAllFiles();
        return ResponseEntity.ok(fileModelList);
    }

}
