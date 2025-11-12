package com.parc.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    private String questionText;
    private String type;
    private String options;
    private String audioData;
    private String audioMimeType;
}

