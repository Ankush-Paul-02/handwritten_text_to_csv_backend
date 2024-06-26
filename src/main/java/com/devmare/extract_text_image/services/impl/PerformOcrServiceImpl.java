package com.devmare.extract_text_image.services.impl;

import com.devmare.extract_text_image.services.FileService;
import com.devmare.extract_text_image.services.PerformOcrService;
import com.devmare.extract_text_image.services.S3FileUploaderService;
import com.devmare.extract_text_image.utils.CsvUtil;
import lombok.Data;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Data
@Service
public class PerformOcrServiceImpl implements PerformOcrService {

    @Autowired
    private final S3FileUploaderService s3FileUploaderService;

    @Autowired
    private final FileService fileService;

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ByteArrayOutputStream writeDataToCSV(String extractedText) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            String[] lines = extractedText.split("\n");
            for (String line : lines) {
                // Check if the line contains a comma
                boolean containsComma = line.contains(",");
                // If a comma is present, replace whitespace with a comma
                if (containsComma) {
                    line = line.trim().replaceAll("\\s+", ",");
                }
                writer.write(line);
                writer.newLine();
            }
            return outputStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String performOCR(MultipartFile file, String outputFileName) {
        Tesseract tesseract = new Tesseract();
        File scannedImage = convertMultipartFileToFile(file);
        ByteArrayOutputStream csvOutputStream = null;

        try {
            tesseract.setDatapath("D:\\Program Files\\Tesseract-OCR\\tessdata");
            String extractedText = tesseract.doOCR(scannedImage);
            csvOutputStream = writeDataToCSV(extractedText);
            String uploadedFileUrl = "";
            if (csvOutputStream != null) {
                MultipartFile multipartFile = CsvUtil.convertCsvToMultipartFile(csvOutputStream.toString(), outputFileName);
                uploadedFileUrl = s3FileUploaderService.uploadImage(multipartFile);
                fileService.uploadFile(uploadedFileUrl);
                System.out.println(uploadedFileUrl);
            }
            return uploadedFileUrl;
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (scannedImage != null && scannedImage.exists()) {
                scannedImage.delete();
            }
            if (csvOutputStream != null) {
                try {
                    csvOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
