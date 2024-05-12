package com.devmare.extract_text_image.repository;

import com.devmare.extract_text_image.models.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Integer> {
}
