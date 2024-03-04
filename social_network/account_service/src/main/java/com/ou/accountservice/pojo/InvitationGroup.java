/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.accountservice.pojo;

import java.io.Serializable;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "invitation_group")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationGroup implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "group_name")
    private String groupName;
    // @OneToMany(mappedBy = "groupId")
    // private Collection<PostInvitation> postInvitationCollection;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupId")
    private List<GroupUser> groupUsers;

    @Override
    public String toString() {
        return "com.ou.pojo.InvitationGroup[ id=" + id + " ]";
    }
    
}
