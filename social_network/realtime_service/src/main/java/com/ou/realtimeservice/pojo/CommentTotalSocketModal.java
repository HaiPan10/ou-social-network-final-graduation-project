package com.ou.realtimeservice.pojo;

import java.util.List;


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
