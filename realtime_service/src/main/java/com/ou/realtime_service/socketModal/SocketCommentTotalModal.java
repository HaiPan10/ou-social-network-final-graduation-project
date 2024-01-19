package com.ou.realtime_service.socketModal;

import java.util.List;

import com.ou.realtime_service.pojo.Comment;

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
