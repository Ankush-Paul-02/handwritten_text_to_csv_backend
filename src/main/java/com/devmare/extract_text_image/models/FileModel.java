package com.devmare.extract_text_image.models;

import jakarta.persistence.*;
import lombok.*;
import org.joda.time.DateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fileUrl;
    private DateTime createdAt;
}
