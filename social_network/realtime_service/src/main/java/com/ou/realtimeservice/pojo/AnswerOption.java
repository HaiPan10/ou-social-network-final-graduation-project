/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.realtimeservice.pojo;

import java.io.Serializable;


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
public class AnswerOption implements Serializable {

    private Long id;

    @JsonIgnore
    private Answer answerId;

    private QuestionOption questionOptionId;

    @Override
    public String toString() {
        return "AnswerOption [questionOptionId=" + questionOptionId + "]";
    }
}
