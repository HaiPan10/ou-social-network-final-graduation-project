package com.ou.social_network.socketModal;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SocketReactionModal {
    private Long interactUser;
    private Map<Long, Long> reactionTotal;
}
