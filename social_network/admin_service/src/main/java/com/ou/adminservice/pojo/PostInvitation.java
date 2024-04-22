/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.adminservice.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author PHONG
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostInvitation implements Serializable {

    private Long id;

    @Size(max = 250)
    private String eventName;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startAt;

    private List<PostInvitationUser> postInvitationUsers;

    // @JoinColumn(name = "group_id", referencedColumnName = "id")
    // @ManyToOne
    private InvitationGroup groupId;

    @JsonIgnore
    private Post post;

    @Override
    public String toString() {
        return "com.ou.pojo.PostInvitation[ id=" + id + " ]";
    }

}
