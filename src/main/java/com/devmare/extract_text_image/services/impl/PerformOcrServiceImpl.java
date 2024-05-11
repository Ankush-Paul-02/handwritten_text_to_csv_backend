package com.devmare.extract_text_image.services.impl;

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

//    private void writeDataToCSV(String extractedText, String csvFilePath) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
//            String[] lines = extractedText.split("\n");
//            for (String line : lines) {
//                // Check if the line contains a comma
//                boolean containsComma = line.contains(",");
//                // If a comma is present, replace whitespace with a comma
//                if (containsComma) {
//                    line = line.trim().replaceAll("\\s+", ",");
//                }
//                writer.write(line);
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private String writeDataToCSV(String extractedText) {
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
//            String[] lines = extractedText.split("\n");
//            for (String line : lines) {
//                // Check if the line contains a comma
//                boolean containsComma = line.contains(",");
//                // If a comma is present, replace whitespace with a comma
//                if (containsComma) {
//                    line = line.trim().replaceAll("\\s+", ",");
//                }
//                writer.write(line);
//                writer.newLine();
//            }
//            return outputStream;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

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


//    @Override
//    public String performOCR(MultipartFile file, String outputFilePath) {
//        Tesseract tesseract = new Tesseract();
//        File scannedImage = convertMultipartFileToFile(file);
//
//        try {
//            tesseract.setDatapath("D:\\Program Files\\Tesseract-OCR\\tessdata");
//            String extractedText = tesseract.doOCR(scannedImage);
//            writeDataToCSV(extractedText, outputFilePath);
//            return "Successfully performed OCR and created CSV file!";
//        } catch (TesseractException e) {
//            return "Error performing OCR: " + e.getMessage();
//        } finally {
//            if (scannedImage != null && scannedImage.exists()) {
//                scannedImage.delete();
//            }
//        }
//    }

    @Override
    public String performOCR(MultipartFile file, String outputFileName) {
        Tesseract tesseract = new Tesseract();
        File scannedImage = convertMultipartFileToFile(file);
        ByteArrayOutputStream csvOutputStream = null;

        try {
            tesseract.setDatapath("D:\\Program Files\\Tesseract-OCR\\tessdata");
            String extractedText = tesseract.doOCR(scannedImage);
            csvOutputStream = writeDataToCSV(extractedText);
            MultipartFile multipartFile = CsvUtil.convertCsvToMultipartFile(csvOutputStream.toString(), outputFileName);
            String uploadedFileUrl = s3FileUploaderService.uploadImage(multipartFile);
            System.out.println(uploadedFileUrl);
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
