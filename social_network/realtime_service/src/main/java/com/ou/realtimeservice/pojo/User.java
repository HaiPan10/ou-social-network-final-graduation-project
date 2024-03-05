package com.ou.realtimeservice.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String avatar;
    public User(Long id) {
        this.id = id;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
