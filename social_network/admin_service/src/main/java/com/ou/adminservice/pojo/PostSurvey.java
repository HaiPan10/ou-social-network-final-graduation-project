/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.adminservice.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PostSurvey implements Serializable {

    @NotNull
    private Long id;

    private Date startAt;

    @Size(max = 100)
    private String surveyTitle;

    @Size(max = 6)
    private String surveyStatus;

    private List<Question> questions;

    @JsonIgnore
    private List<Response> responses;

    @JsonIgnore
    private Post post;

    @Override
    public String toString() {
        return "com.ou.pojo.PostSurvey[ id=" + id + " ]";
    }
    // @Override
    // public String toString() {
    //     return "PostSurvey [surveyTitle=" + surveyTitle + ", surveyStatus=" + surveyStatus + ", questions=" + questions
    //             + "]";
    // }

}
