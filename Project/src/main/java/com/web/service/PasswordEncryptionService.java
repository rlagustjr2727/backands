package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.repository.UserRepository;
import com.web.user.User;

@Service
public class PasswordEncryptionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void encryptPasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String encodedPassword = passwordEncoder.encode(user.getUserPassword());
            user.setUserPassword(encodedPassword);
            userRepository.save(user);
        }
    }
}
