package com.ou.mailservice.pojo;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserStudent implements Serializable{
    private Long id;
    private String studentIdentical;
    private User user;

    @Override
    public String toString() {
        return "UserStudent [id=" + id + ", studentIdentical=" + studentIdentical + "]";
    }

    public UserStudent(String studentIdentical) {
        this.studentIdentical = studentIdentical;
    }
}
