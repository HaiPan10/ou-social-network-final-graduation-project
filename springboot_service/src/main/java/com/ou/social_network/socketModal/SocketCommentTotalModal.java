package com.ou.social_network.socketModal;

import java.util.List;

import com.ou.social_network.pojo.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SocketCommentTotalModal {
    private Integer commentTotal;
    private List<Comment> latestComment;
}
