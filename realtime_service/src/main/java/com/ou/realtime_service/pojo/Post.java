package com.ou.realtime_service.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author PHONG
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post")
@ToString
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255, message = "{post.content.invalidSize}")
    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    @Column(name = "is_active_comment")
    private Boolean isActiveComment;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "postId")
    private List<ImageInPost> imageInPostList;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "postId")
    private List<PostReaction> postReactionList;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "postId")
    private List<Comment> commentList;

    @Transient
    private Map<Long, Long> reactionTotal;
    @Transient
    private Integer commentTotal;
    // @Override
    // public String toString() {
    //     return "Post [id=" + id + ", content=" + content + ", isActiveComment=" + isActiveComment + ", imageInPostList="
    //             + imageInPostList + "]";
    // }
    @Transient
    private Reaction currentReaction;

    // @Transient
    // private List<Comment> twoComments;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "post")
    private PostSurvey postSurvey;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "post")
    private PostInvitation postInvitation;

    public Post(Long id){
        this.id = id;
    }

    public Post(Post post, PostInvitation postInvitation) {
        this.id = post.getId();
        this.postInvitation = postInvitation;
    }
}
