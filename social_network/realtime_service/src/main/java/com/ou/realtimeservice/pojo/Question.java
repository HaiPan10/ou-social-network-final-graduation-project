/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.realtimeservice.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author PHONG
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
    private Long id;

    private String questionText;

    private Boolean isMandatory;

    private Integer questionOrder;

    private List<QuestionOption> questionOptions;

    @JsonIgnore
    private List<Answer> answers;

    @JsonIgnore
    private PostSurvey surveyId;

    private QuestionType questionTypeId;

    @Override
    public String toString() {
        return "Question [id=" + id + "]";
    }
}
