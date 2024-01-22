package com.ou.realtime_service.modals;

import java.util.List;

import com.ou.realtime_service.DTOs.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentTotalSocketModal {
    private Integer commentTotal;
    private List<Comment> latestComment;
}
