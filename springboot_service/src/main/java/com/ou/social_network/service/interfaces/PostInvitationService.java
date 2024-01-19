package com.ou.social_network.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ou.social_network.pojo.PostInvitation;
import com.ou.social_network.pojo.User;

public interface PostInvitationService {
    PostInvitation create(Long postId, PostInvitation postInvitation, List<User> listUsers);
    List<Object[]> stat(Map<String, String> params);
}
