package com.parc.survey.controller;



import com.parc.survey.dto.SubmitResponseRequest;

import com.parc.survey.dto.SurveyRequest;
import com.parc.survey.dto.SurveyResponse;
import com.parc.survey.dto.ReportResponse;
import com.parc.survey.service.SurveyService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SurveyController {
    
    @Autowired
    private SurveyService surveyService;
    
    @GetMapping
    public ResponseEntity<List<SurveyResponse>> getAllSurveys() {
        
        return ResponseEntity.ok(surveyService.getAllSurveys());
        
    }
    
    @PostMapping
    public ResponseEntity<SurveyResponse> createSurvey(
            @RequestBody SurveyRequest request,
            @RequestParam Long userId) {
        try {
            SurveyResponse response = surveyService.createSurvey(request, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{surveyId}/responses")
    public ResponseEntity<String> submitResponse(
            @PathVariable Long surveyId,
            @RequestBody SubmitResponseRequest request,
            @RequestParam Long userId) {
        try {
            surveyService.submitResponse(surveyId, request, userId);
            return ResponseEntity.ok("Response submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{surveyId}/report")
    public ResponseEntity<ReportResponse> getReport(
            @PathVariable Long surveyId,
            @RequestParam Long userId) {
        try {
            ReportResponse report = surveyService.getReport(surveyId, userId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}