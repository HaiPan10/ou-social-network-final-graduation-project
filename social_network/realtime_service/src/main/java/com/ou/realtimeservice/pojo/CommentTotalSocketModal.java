package com.ou.realtimeservice.pojo;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentTotalSocketModal {
    private Integer commentTotal;
    private List<Comment> latestComment;
}
