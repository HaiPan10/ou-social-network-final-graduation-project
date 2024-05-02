/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.realtimeservice.pojo;

import java.io.Serializable;
import java.util.Collection;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author PHONG
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer implements Serializable {

    private Long id;

    private String value;

    private Question questionId;

    @JsonIgnore
    private Response responseId;

    private Collection<AnswerOption> answerOptions;

    @Override
    public String toString() {
        return "Answer [value=" + value + ", questionId=" + questionId + ", answerOptions=" + answerOptions + "]";
    }
}
