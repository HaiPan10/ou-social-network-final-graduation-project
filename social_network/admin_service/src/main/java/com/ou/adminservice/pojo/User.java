package com.ou.adminservice.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class User implements Serializable{
    private Long id;

    @NotBlank(message = "{user.firstName.notBlank}")
    @NotNull
    @Size(min = 1, max = 45, message = "{user.firstName.invalidSize}")
    private String firstName;

    @NotBlank(message = "{user.lastName.notBlank}")
    @NotNull
    @Size(min = 1, max = 45, message = "{user.lastName.invalidSize}")
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Size(max = 300)
    private String avatar;

    @Size(max = 300)
    private String coverAvatar;

    @JsonIgnore
    private Account account;

    private UserStudent userStudent;

    @Override
    public String toString() {
        return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob + ", avatar="
                + avatar + ", coverAvatar=" + coverAvatar + ", userStudent=" + userStudent + "]";
    }

    public User(Long id) {
        this.id = id;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
