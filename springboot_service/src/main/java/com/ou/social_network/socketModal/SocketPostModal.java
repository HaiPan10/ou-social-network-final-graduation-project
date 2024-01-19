package com.ou.social_network.socketModal;

import com.ou.social_network.pojo.Post;

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
