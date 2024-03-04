package com.ou.mailservice.pojo;

import java.io.Serializable;
import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author PHONG
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account implements Serializable{
    private Long id;
    private String email;
    private String password;
    private Date createdDate;
    private Date resetPasswordDate;
    private String verificationCode;
    private String status;
    private User user;
    private Role roleId;

    public Account(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
