package com.ou.realtimeservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadAvatarEvent{
    private Long userId;
    private String newPhotoUrl;
}