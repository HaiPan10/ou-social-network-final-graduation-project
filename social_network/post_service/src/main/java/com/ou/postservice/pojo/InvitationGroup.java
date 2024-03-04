/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.postservice.pojo;

import java.io.Serializable;

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
public class InvitationGroup implements Serializable {
    private Long id;
    private String groupName;

    @Override
    public String toString() {
        return "com.ou.pojo.InvitationGroup[ id=" + id + " ]";
    }
}
