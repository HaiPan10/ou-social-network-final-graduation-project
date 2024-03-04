package com.ou.accountservice.repository.repositoryJPA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ou.accountservice.pojo.User;

public interface UserRepositoryJPA extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.id IN ?1")
    List<User> list(List<Long> listUserId);
}
