package com.ou.accountservice.pojo;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_student")
public class UserStudent implements Serializable{
    @Id
    // @NotNull
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "{userStudent.studentIdentical.notBlank}")
    @NotNull
    @Size(min = 10, max = 10, message = "{userStudent.studentIdentical.invalid}")
    @Column(name = "student_identical")
    private String studentIdentical;

    @JsonIgnore
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Override
    public String toString() {
        return "UserStudent [id=" + id + ", studentIdentical=" + studentIdentical + "]";
    }

    public UserStudent(String studentIdentical) {
        this.studentIdentical = studentIdentical;
    }
}
