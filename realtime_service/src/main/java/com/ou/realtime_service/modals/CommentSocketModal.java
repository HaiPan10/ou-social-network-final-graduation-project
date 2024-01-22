package com.ou.realtime_service.modals;

import com.ou.realtime_service.DTOs.Comment;

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
