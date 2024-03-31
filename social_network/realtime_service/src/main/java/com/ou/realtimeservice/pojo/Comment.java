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
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedDate;
    private Long postId;
    private Post post;
    private Long userId;
    private User user;
    private Comment parentComment;
    private Long repliedUserId;
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

    @Override
    public String toString() {
        return "Comment [id=" + id + ", content=" + content + ", createdDate=" + createdDate + ", updatedDate="
                + updatedDate + ", postId=" + postId + ", userId=" + userId + ", user=" + user + ", level=" + level
                + "]";
    }
}
