package com.parc.survey.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 1000)
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    private QuestionType type;
    
    @Column(length = 500)
    private String options;
    
    @Lob
    @Column(columnDefinition = "CLOB")
    private String audioData;
    
    private String audioMimeType;
    
    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();
    
    public enum QuestionType {
        TEXT, MULTIPLE_CHOICE, RATING, YES_NO, AUDIO
    }
}