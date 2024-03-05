package com.ou.realtimeservice.pojo;

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
    private List<ImageInPost> imageInPostList;
    private User userId;
    private List<PostReaction> postReactionList;
    private List<Comment> commentList;
    private Map<Long, Long> reactionTotal;
    private Integer commentTotal;
    private Reaction currentReaction;
    private PostSurvey postSurvey;
    private PostInvitation postInvitation;

    public Post(Long id){
        this.id = id;
    }

    public Post(Post post, PostInvitation postInvitation) {
        this.id = post.getId();
        this.postInvitation = postInvitation;
    }
}
