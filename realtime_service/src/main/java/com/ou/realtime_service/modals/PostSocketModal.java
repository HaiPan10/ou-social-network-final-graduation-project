package com.ou.realtime_service.modals;

import com.ou.realtime_service.DTOs.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSocketModal {
    private Post post;
    private String action;
}
