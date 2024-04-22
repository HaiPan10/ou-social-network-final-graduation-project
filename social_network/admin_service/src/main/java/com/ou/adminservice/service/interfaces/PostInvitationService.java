package com.ou.adminservice.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ou.adminservice.pojo.PostInvitation;
import com.ou.adminservice.pojo.User;

public interface PostInvitationService {
    PostInvitation create(Long postId, PostInvitation postInvitation, List<User> listUsers);
    Object[][] stat(Map<String, String> params);
}
