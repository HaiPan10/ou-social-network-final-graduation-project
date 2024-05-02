package com.ou.realtimeservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEvent {
    private String orderAction;
    private Long postId;
    private String realtimeAction;
}