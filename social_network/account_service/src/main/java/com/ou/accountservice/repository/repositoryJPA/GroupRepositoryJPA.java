package com.ou.accountservice.repository.repositoryJPA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ou.accountservice.pojo.InvitationGroup;

public interface GroupRepositoryJPA extends JpaRepository<InvitationGroup, Long>{
    @Query("SELECT u.id, a.email, CONCAT(u.lastName, ' ', u.firstName), u.avatar " +
           "FROM InvitationGroup ig " +
           "JOIN ig.groupUsers gu " +
           "JOIN gu.userId u " +
           "JOIN u.account a " +
           "WHERE ig.id = ?1")
    List<Object[]> getUsers(Long groupId);
}
