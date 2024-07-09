package com.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.repository.UserRepository;
import com.web.user.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(User user) {
        if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        String encodedPassword = passwordEncoder.encode(user.getUserPassword());
        logger.info("Encoded password for user {}: {}", user.getUserId(), encodedPassword);
        user.setUserPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public User findByUserIdAndUserPassword(String userId, String userPassword) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            boolean matches = passwordEncoder.matches(userPassword, user.getUserPassword());
            logger.info("Password matches for user {}: {}", userId, matches);
            if (matches) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean isUserIdExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public boolean isUserNickNameExists(String userNickName) {
        return userRepository.existsByUserNickName(userNickName);
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
