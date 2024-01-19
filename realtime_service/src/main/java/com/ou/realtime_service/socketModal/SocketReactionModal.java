package com.ou.realtime_service.socketModal;

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
