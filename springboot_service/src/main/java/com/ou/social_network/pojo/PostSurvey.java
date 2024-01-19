/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.social_network.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Entity
@Table(name = "post_survey")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSurvey implements Serializable {
    @Id
    @NotNull
    @Column(name = "id")
    private Long id;

    @Column(name = "start_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;
    @Size(max = 100)
    
    @Column(name = "survey_title")
    private String surveyTitle;
    @Size(max = 6)
    @Column(name = "survey_status")
    private String surveyStatus;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "surveyId")
    private List<Question> questions;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "surveyId")
    private List<Response> responses;
    
    @JsonIgnore
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
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
