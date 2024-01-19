package com.ou.social_network.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ou.social_network.pojo.Role;
import java.util.Optional;

public interface RoleRepositoryJPA extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long id);
}
