package com.ou.realtimeservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentSocketModal {
    private Comment comment;
    private String action;
}
