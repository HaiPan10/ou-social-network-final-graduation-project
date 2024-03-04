package com.ou.postservice.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ou.postservice.pojo.PostInvitation;
import com.ou.postservice.pojo.User;

public interface PostInvitationService {
    PostInvitation create(Long postId, PostInvitation postInvitation, List<User> listUsers);
    List<Object[]> stat(Map<String, String> params);
}
