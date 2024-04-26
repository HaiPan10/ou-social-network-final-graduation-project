package com.ou.realtimeservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEvent{
    private Long commentId;
    private Long postId;
    private String action;
}