package com.ou.realtime_service.socketModal;

import com.ou.realtime_service.pojo.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SocketPostModal {
    private Post post;
    private String action;
}
