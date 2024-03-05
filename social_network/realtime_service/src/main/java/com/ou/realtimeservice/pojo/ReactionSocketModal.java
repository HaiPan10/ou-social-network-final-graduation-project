package com.ou.realtimeservice.pojo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReactionSocketModal {
    private Long interactUser;
    private Map<Long, Long> reactionTotal;
}
