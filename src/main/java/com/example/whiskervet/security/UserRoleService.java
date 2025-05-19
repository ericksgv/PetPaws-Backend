package com.example.whiskervet.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.whiskervet.entity.Role;
import com.example.whiskervet.entity.UserEntity;
import com.example.whiskervet.repository.RepositorioUserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class UserRoleService {

    @Autowired
    private RepositorioUserEntity userRepository;

    public List<Role> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByUsername(authentication.getName()).get();
        return user.getRoles();
    }
}
