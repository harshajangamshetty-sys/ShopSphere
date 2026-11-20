package com.harsha.shopsphere.user.dto;

import com.harsha.shopsphere.user.Role;
import com.harsha.shopsphere.user.entity.User;

import java.time.Instant;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        Role role,
        boolean active,
        Instant createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(),
                user.getFullName(), user.getRole() , user.isActive() ,
                user.getCreatedAt());
    }
}
