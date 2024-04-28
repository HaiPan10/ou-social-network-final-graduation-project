package com.ou.adminservice.service.interfaces;

public interface QuestionService {
    Object[][] stat(Long questionId);
    Integer countUnchoiceOption(Long questionId);
    String getText(Long id);
}
