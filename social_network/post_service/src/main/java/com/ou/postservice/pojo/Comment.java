package com.ou.postservice.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private Comment parentComment;
    private User repliedUser;
    private List<Comment> replies;
    private Integer level;
    private Long repliesTotal;
    private Comment firstReply;

    public Comment(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Comment(Long id) {
        this.id = id;
    }

    
    
}
