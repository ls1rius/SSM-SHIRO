package com.cm.service;

import com.cm.entity.Permission;
import com.cm.entity.User;

import java.util.List;

public interface ShiroService {
    User getUserByUserName(String username);
    List<Permission> getPermissionsByUser(User user);
}
