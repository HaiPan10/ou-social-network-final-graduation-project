package com.ou.realtimeservice.pojo;


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
