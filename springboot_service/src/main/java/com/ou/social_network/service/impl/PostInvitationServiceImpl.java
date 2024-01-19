package com.ou.social_network.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ou.social_network.pojo.PostInvitation;
import com.ou.social_network.pojo.PostInvitationUser;
import com.ou.social_network.pojo.User;
import com.ou.social_network.repository.repositoryJPA.PostInvitationRepositoryJPA;
import com.ou.social_network.service.interfaces.PostInvitationService;

@Service
public class PostInvitationServiceImpl implements PostInvitationService {

    @Autowired
    private PostInvitationRepositoryJPA postInvitationRepositoryJPA;

    @Override
    public PostInvitation create(Long postId, PostInvitation postInvitation, List<User> listUsers) {
        postInvitation.setId(postId);
        // return postInvitationRepository.create(postInvitation, listUsers);
        // postInvitation.setId((Long) session.save(postInvitation));
        if (listUsers != null) {
            List<PostInvitationUser> list = new ArrayList<>();
            listUsers.stream().forEach(u -> {
                PostInvitationUser p = new PostInvitationUser();
                p.setPostInvitationId(postInvitation);
                p.setUserId(u);
                list.add(p);
            });
            postInvitation.setPostInvitationUsers(list);
        }
        return postInvitationRepositoryJPA.save(postInvitation);
    }

    @Override
    public List<Object[]> stat(Map<String, String> params) {
        boolean byMonth = params.get("byMonth") != null ? true : false;
        boolean byQuarter = params.get("byQuarter") != null ? true : false;
        Integer year = params.get("year") != null ? Integer.valueOf(params.get("year")) : null;
        return postInvitationRepositoryJPA.stat(year, byMonth, byQuarter);
    }
    
}
