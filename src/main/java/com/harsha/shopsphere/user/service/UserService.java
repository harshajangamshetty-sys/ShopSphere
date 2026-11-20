package com.harsha.shopsphere.user.service;


import com.harsha.shopsphere.common.exception.DuplicateResourceException;
import com.harsha.shopsphere.user.dto.RegisterRequest;
import com.harsha.shopsphere.user.dto.UserResponse;
import com.harsha.shopsphere.user.entity.User;
import com.harsha.shopsphere.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if(userRepository.existsByEmail(email)){
            throw new DuplicateResourceException("User with email " + email + " already exists");
        }
        String passwordHash = passwordEncoder.encode(request.password());
        User user = new User(email , passwordHash , request.fullName().trim());
        userRepository.save(user);
        return UserResponse.from(user);
    }
}
