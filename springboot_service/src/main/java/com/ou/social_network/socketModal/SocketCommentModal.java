package com.ou.social_network.socketModal;

import com.ou.social_network.pojo.Comment;

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
