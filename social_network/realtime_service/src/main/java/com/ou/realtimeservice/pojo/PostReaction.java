package com.ou.realtimeservice.pojo;

import java.io.Serializable;
import java.util.Date;

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
public class PostReaction implements Serializable {
    private Long id;
    private Date createdAt;
    private Post postId;
    private Reaction reactionId;
    private User userId;

    @Override
    public String toString() {
        return "PostReaction [id=" + id + ", createdAt=" + createdAt + ", reactionId="
                + reactionId + ", userId=" + userId + "]";
    }
    
    
}
