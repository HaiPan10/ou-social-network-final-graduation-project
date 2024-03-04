/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.postservice.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author PHONG
 */
@Entity
@Table(name = "post_invitation")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostInvitation implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 250)
    @Column(name = "event_name")
    private String eventName;
    
    @Column(name = "start_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postInvitationId")
    private List<PostInvitationUser> postInvitationUsers;

    // @JoinColumn(name = "group_id", referencedColumnName = "id")
    // @ManyToOne
    @Transient
    private InvitationGroup groupId;

    @JsonIgnore
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Post post;

    @Override
    public String toString() {
        return "com.ou.pojo.PostInvitation[ id=" + id + " ]";
    }
    
}
