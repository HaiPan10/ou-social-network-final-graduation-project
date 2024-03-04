package com.ou.mailservice.pojo;

import java.io.Serializable;
import java.util.Date;

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
public class User implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Date dob;
    private String avatar;
    private String coverAvatar;
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
