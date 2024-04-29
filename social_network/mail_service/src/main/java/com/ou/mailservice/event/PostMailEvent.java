package com.ou.mailservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMailEvent {
    private String email;
    private String eventName;
    private String content;
}