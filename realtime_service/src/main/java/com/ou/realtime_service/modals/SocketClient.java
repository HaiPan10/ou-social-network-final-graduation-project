package com.ou.realtime_service.modals;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocketClient implements Principal {
    private String name;
    private Long id;
}
