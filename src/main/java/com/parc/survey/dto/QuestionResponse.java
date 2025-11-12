package com.parc.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String questionText;
    private String type;
    private String options;
    private String audioData;
    private String audioMimeType;
}