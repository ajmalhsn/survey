package com.parc.survey.domain;

import jakarta.persistence.Id;

import com.parc.survey.domain.SurveyResponse;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;


@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    
    @ManyToOne
    @JoinColumn(name = "response_id")
    private SurveyResponse response;
    
    @Column(length = 5000)
    private String answerText;
    
    private String audioMimeType;
}
