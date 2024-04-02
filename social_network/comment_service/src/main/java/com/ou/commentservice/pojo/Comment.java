package com.ou.commentservice.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author PHONG
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "{comment.content.notNull}")
    @Size(min = 1, max = 255, message = "{comment.content.invalidSize}")
    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedDate;

    @Column(name = "post_id")
    private Long postId;
    
    @Transient
    private Post post;

    @Column(name = "user_id")
    private Long userId;

    @Transient
    private User user;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id",nullable = true)
    private Comment parentComment;

    @Column(name = "replied_user_id")
    private Long repliedUserId;

    @Transient
    private User repliedUser;

    @JsonIgnore
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    private List<Comment> replies;

    @Column(name = "level")
    private Integer level;

    @Transient
    private Long repliesTotal;

    @Transient
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
                + updatedDate + ", postId=" + postId + ", userId=" + userId + ", user=" + user + ", repliedUserId="
                + repliedUserId + ", repliedUser=" + repliedUser + ", level=" + level + "]";
    }
    
}
