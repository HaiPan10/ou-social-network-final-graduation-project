/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.realtime_service.DTOs;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "question")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 2000)
    @Column(name = "question_text")
    private String questionText;

    @Column(name = "is_mandatory")
    private Boolean isMandatory;

    @Column(name = "question_order")
    private Integer questionOrder;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "questionId")
    private List<QuestionOption> questionOptions;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionId")
    private List<Answer> answers;

    @JsonIgnore
    @JoinColumn(name = "survey_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PostSurvey surveyId;

    @JoinColumn(name = "question_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private QuestionType questionTypeId;

    @Override
    public String toString() {
        return "Question [id=" + id + "]";
    }
}
