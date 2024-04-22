/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.adminservice.pojo;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnore;

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
@ToString(exclude = {"postInvitationId"})
public class PostInvitationUser implements Serializable {

    private Long id;

    @JsonIgnore
    private PostInvitation postInvitationId;

    private Long userId;

    private User user;

    // @Override
    // public String toString() {
    //     return "User[ id=" + userId + " ]";
    // }

}
