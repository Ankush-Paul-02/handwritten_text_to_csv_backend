package com.devmare.extract_text_image.payloads;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppApiResponse {
    private String message;
    private boolean isSuccess;
}