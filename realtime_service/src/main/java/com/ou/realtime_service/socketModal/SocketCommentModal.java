package com.ou.realtime_service.socketModal;

import com.ou.realtime_service.pojo.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SocketCommentModal {
    private Comment comment;
    private String action;
}
