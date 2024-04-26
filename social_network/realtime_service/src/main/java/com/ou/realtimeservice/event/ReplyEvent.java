package com.ou.realtimeservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyEvent{
    private Long commentId;
    private Long parentCommentId;
    private String action;
}