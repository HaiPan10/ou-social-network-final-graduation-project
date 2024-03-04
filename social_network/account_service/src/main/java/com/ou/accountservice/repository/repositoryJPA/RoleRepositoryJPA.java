package com.ou.accountservice.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ou.accountservice.pojo.Role;
import java.util.Optional;

public interface RoleRepositoryJPA extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long id);
}
