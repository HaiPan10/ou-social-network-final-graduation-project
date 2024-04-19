package com.ou.adminservice.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "{comment.content.notNull}")
    @Size(min = 1, max = 255, message = "{comment.content.invalidSize}")
    private String content;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedDate;

    @JsonIgnore
    private Post postId;

    private User userId;

    @JsonIgnore
    private Comment parentComment;

    private User repliedUser;

    @JsonIgnore
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
