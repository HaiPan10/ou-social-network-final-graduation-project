package com.ou.adminservice.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account implements Serializable{

    private Long id;

    @NotBlank(message = "{account.email.notBlank}")
    @Email(message = "{account.email.invalid}")
    @Size(min = 1, message = "{account.email.invalidSize}")
    private String email;

    @NotBlank(message = "{account.password.notBlank}")
    @Size(min = 6, message = "{account.password.invalidSize}")
    private String password;

    private String confirmPassword;

    private Date createdDate;

    private Date resetPasswordDate;

    @Size(max = 64)
    private String verificationCode;

    @Size(max = 30)
    private String status;

    private User user;

    @JsonIgnore
    private Role roleId;

    public Account(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Account(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
