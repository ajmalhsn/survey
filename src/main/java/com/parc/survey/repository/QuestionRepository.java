package com.parc.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parc.survey.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}