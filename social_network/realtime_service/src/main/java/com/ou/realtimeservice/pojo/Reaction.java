package com.ou.realtimeservice.pojo;

import java.io.Serializable;
import java.util.List;


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
public class Reaction implements Serializable {
    private Long id;
    private String name;
    private List<PostReaction> postReactionList;

    public Reaction(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Reaction [id=" + id + ", name=" + name + "]";
    }

    
}
