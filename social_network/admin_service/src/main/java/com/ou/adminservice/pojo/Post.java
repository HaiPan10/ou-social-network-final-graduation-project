package com.ou.adminservice.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Post implements Serializable {

    private Long id;

    @Size(max = 255, message = "{post.content.invalidSize}")
    private String content;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    private Boolean isActiveComment;

    private List<ImageInPost> imageInPostList;

    private Long userId;

    private User user;

    // @JsonIgnore
    // private List<PostReaction> postReactionList;

    private List<Comment> commentList;

    private Map<Long, Long> reactionTotal;

    private Integer commentTotal;
    // @Override
    // public String toString() {
    //     return "Post [id=" + id + ", content=" + content + ", isActiveComment=" + isActiveComment + ", imageInPostList="
    //             + imageInPostList + "]";
    // }
    // @Transient
    // private Reaction currentReaction;

    // @Transient
    // private List<Comment> twoComments;

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