/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.postservice.pojo;

import java.io.Serializable;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "question_option")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOption implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "value")
    private String value;
    
    @Column(name = "question_order")
    private Integer questionOrder;

    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Question questionId;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionOptionId")
    private List<AnswerOption> answerOptions;

    @Override
    public String toString() {
        return "QuestionOption [id=" + id + "]";
    }

    
}
