package com.ou.accountservice.service.interfaces;

import com.ou.accountservice.pojo.Role;

public interface RoleService {
    Role retrieve(Long id) throws Exception;
}
