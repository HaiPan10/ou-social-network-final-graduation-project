package com.ou.adminservice.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserStudent implements Serializable {
    private Long id;

    @NotBlank(message = "{userStudent.studentIdentical.notBlank}")
    @NotNull
    @Size(min = 10, max = 10, message = "{userStudent.studentIdentical.invalid}")
    private String studentIdentical;

    @JsonIgnore
    private User user;

    @Override
    public String toString() {
        return "UserStudent [id=" + id + ", studentIdentical=" + studentIdentical + "]";
    }

    public UserStudent(String studentIdentical) {
        this.studentIdentical = studentIdentical;
    }
}
