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
public class QuestionOption implements Serializable {

    private Long id;
    private String value;

    private Integer questionOrder;

    @JsonIgnore
    private Question questionId;

    @JsonIgnore
    private List<AnswerOption> answerOptions;

    @Override
    public String toString() {
        return "QuestionOption [id=" + id + "]";
    }


}
