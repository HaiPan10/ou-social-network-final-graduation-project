package com.ou.realtimeservice.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDocEvent {
    private Long userId;
    private String displayName;
    private String photoUrl;
    private String activeStatus;
}