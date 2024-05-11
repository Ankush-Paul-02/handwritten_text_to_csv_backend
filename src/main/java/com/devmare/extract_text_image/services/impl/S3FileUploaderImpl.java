package com.devmare.extract_text_image.services.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.devmare.extract_text_image.exceptions.FileUploadException;
import com.devmare.extract_text_image.services.S3FileUploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class S3FileUploaderImpl implements S3FileUploaderService {

    @Autowired
    private AmazonS3 client;

    @Override
    public String uploadImage(MultipartFile file) {

        if (file == null) {
            throw new FileUploadException("File is null!");
        }

        // file.csv
        String actualFileName = file.getOriginalFilename();
        // xyzfile.csv
        String newFileName = UUID.randomUUID() + actualFileName.substring(actualFileName.lastIndexOf("."));

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(file.getSize());

        try {
            client.putObject(new PutObjectRequest("prakaushal", newFileName, file.getInputStream(), metaData));
            return preSignedUrl(newFileName);
        } catch (IOException e) {
            throw new FileUploadException("Error in uploading image " + e.getMessage());
        }
    }

    @Override
    public List<String> allFiles() {
        ListObjectsV2Request request = new ListObjectsV2Request().withBucketName("prakaushal");
        ListObjectsV2Result listObjectsV2Result = client.listObjectsV2(request);

        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();
        return objectSummaries.stream().map(
                item -> preSignedUrl(item.getKey())
        ).collect(Collectors.toList());
    }

    @Override
    public String preSignedUrl(
            String fileName
    ) {
        Date expirationDate = new Date();
        int hour = 2;
        long time = expirationDate.getTime();
        time = time + hour * 60 * 60 * 1000;
        expirationDate.setTime(time);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(
                "prakaushal",
                fileName
        );
        URL url = client.generatePresignedUrl(request);
        return url.toString();
    }
}
