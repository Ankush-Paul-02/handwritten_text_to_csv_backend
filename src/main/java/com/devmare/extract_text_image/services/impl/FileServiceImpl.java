package com.devmare.extract_text_image.services.impl;

import com.devmare.extract_text_image.models.FileModel;
import com.devmare.extract_text_image.repository.FileRepository;
import com.devmare.extract_text_image.services.FileService;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public void uploadFile(String fileUrl) {
        FileModel newFile = FileModel.builder()
                .fileUrl(fileUrl)
                .createdAt(DateTime.now())
                .build();
        fileRepository.save(newFile);
    }

    @Override
    public List<FileModel> getAllFiles() {
        return fileRepository.findAll();
    }
}
