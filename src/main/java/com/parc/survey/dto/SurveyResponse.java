package com.parc.survey.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SurveyResponse {
    private Long id;
    private String title;
    private String description;
    private List<QuestionResponse> questions;
}