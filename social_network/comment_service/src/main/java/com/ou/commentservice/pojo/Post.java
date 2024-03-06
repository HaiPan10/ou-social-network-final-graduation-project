package com.ou.commentservice.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author PHONG
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Post implements Serializable {
    private Long id;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isActiveComment;
    private User userId;
    private List<Comment> commentList;
    private Map<Long, Long> reactionTotal;
    private Integer commentTotal;

    public Post(Long id){
        this.id = id;
    }
}
