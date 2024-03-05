package com.ou.realtimeservice.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class Comment implements Serializable {
    private Long id;
    private String content;
    private Date createdDate;
    private Date updatedDate;
    private Post postId;
    private User userId;
    private Integer level;

    public Comment(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Comment(Long id) {
        this.id = id;
    }

    
    
}