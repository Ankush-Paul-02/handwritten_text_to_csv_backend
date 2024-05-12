package com.devmare.extract_text_image.services;

import com.devmare.extract_text_image.models.FileModel;

import java.util.List;

public interface FileService {

    void uploadFile(String fileUrl);

    List<FileModel> getAllFiles();
}
