package com.ou.accountservice.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.accountservice.pojo.UserStudent;
import java.util.Optional;


public interface UserStudentRepositoryJPA extends JpaRepository<UserStudent, Long> {
    Optional<UserStudent> findByStudentIdentical(String studentIdentical);
}
