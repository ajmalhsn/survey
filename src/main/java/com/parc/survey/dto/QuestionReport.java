package com.parc.survey.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionReport {
    private String questionText;
    private String questionType;
    private Map<String, Long> answerCounts;
    private List<String> allAnswers;
    private List<String> audioAnswers;
}
