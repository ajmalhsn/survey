package com.parc.survey.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parc.survey.domain.Answer;
import com.parc.survey.domain.Question;
import com.parc.survey.domain.Survey;
import com.parc.survey.domain.User;
import com.parc.survey.dto.SurveyResponse;
import com.parc.survey.dto.QuestionReport;
import com.parc.survey.dto.ReportResponse;
import com.parc.survey.dto.SurveyRequest;
import com.parc.survey.dto.QuestionRequest;
import com.parc.survey.dto.QuestionResponse;
import com.parc.survey.dto.SubmitResponseRequest;
import com.parc.survey.dto.AnswerRequest;
import com.parc.survey.repository.AnswerRepository;
import com.parc.survey.repository.QuestionRepository;
import com.parc.survey.repository.SurveyRepository;
import com.parc.survey.repository.SurveyResponseRepository;
import com.parc.survey.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {
    @Autowired
    private final SurveyRepository surveyRepository;
    @Autowired
    private final QuestionRepository questionRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final SurveyResponseRepository responseRepository;
    @Autowired
    private final AnswerRepository answerRepository;
    
    public List<SurveyResponse> getAllSurveys() {
        return surveyRepository.findAll().stream()
            .map(this::mapToSurveyResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public SurveyResponse createSurvey(SurveyRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admins can create surveys");
        }
        
        Survey survey = new Survey();
        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());
        survey.setCreator(user);
        
        Survey savedSurvey = surveyRepository.save(survey);
        
        for (QuestionRequest qr : request.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(qr.getQuestionText());
            question.setType(Question.QuestionType.valueOf(qr.getType()));
            question.setOptions(qr.getOptions());
            question.setAudioData(qr.getAudioData());
            question.setAudioMimeType(qr.getAudioMimeType());
            question.setSurvey(savedSurvey);
            questionRepository.save(question);
        }
        
        return mapToSurveyResponse(surveyRepository.findById(savedSurvey.getId()).get());
    }
    
    @Transactional
    public void submitResponse(Long surveyId, SubmitResponseRequest request, Long userId) {
        Survey survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new RuntimeException("Survey not found"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Create the ENTITY (not DTO) for SurveyResponse
        com.parc.survey.domain.SurveyResponse responseEntity = new com.parc.survey.domain.SurveyResponse();
        // Don't set ID - let it auto-generate
        responseEntity.setSurvey(survey);
        responseEntity.setUser(user);
        
        // Save the response entity
        com.parc.survey.domain.SurveyResponse savedResponse = responseRepository.save(responseEntity);
        
        // Create answers for each question
        for (AnswerRequest ar : request.getAnswers()) {
            Question question = questionRepository.findById(ar.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));
            
            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setResponse(savedResponse);
            answer.setAnswerText(ar.getAnswerText());
        
            answer.setAudioMimeType(ar.getAudioMimeType());
            answerRepository.save(answer);
        }
    }
    
    public ReportResponse getReport(Long surveyId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admins can view reports");
        }
        
        Survey survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new RuntimeException("Survey not found"));
        
        ReportResponse report = new ReportResponse();
        report.setSurveyTitle(survey.getTitle());
        report.setTotalResponses(survey.getResponses().size());
        
        List<QuestionReport> questionReports = new ArrayList<>();
        
        for (Question question : survey.getQuestions()) {
            QuestionReport qr = new QuestionReport();
            qr.setQuestionText(question.getQuestionText());
            qr.setQuestionType(question.getType().name());
            
            List<Answer> answers = question.getAnswers();
            
            if (question.getType() == Question.QuestionType.TEXT) {
                qr.setAllAnswers(answers.stream()
                    .map(Answer::getAnswerText)
                    .collect(Collectors.toList()));
                qr.setAnswerCounts(new HashMap<>());
            } else if (question.getType() == Question.QuestionType.AUDIO) {
                qr.setAudioAnswers(answers.stream()
                    .map(Answer::getAnswerText)
                    .collect(Collectors.toList()));
                qr.setAnswerCounts(new HashMap<>());
                qr.setAllAnswers(new ArrayList<>());
            } else {
                Map<String, Long> counts = answers.stream()
                    .collect(Collectors.groupingBy(
                        Answer::getAnswerText,
                        Collectors.counting()
                    ));
                qr.setAnswerCounts(counts);
                qr.setAllAnswers(new ArrayList<>());
            }
            
            questionReports.add(qr);
        }
        
        report.setQuestionReports(questionReports);
        return report;
    }
    
    private SurveyResponse mapToSurveyResponse(Survey survey) {
        List<QuestionResponse> questions = survey.getQuestions().stream()
            .map(q -> new QuestionResponse(
                q.getId(),
                q.getQuestionText(),
                q.getType().name(),
                q.getOptions(),
                q.getAudioData(),
                q.getAudioMimeType()
            ))
            .collect(Collectors.toList());
        
        return new SurveyResponse(
            survey.getId(),
            survey.getTitle(),
            survey.getDescription(),
            questions
        );
    }
}