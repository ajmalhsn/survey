package com.parc.survey.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyRequest {
    private String title;
    private String description;
    private List<QuestionRequest> questions;
}

