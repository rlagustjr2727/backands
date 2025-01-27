package com.web.controller;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.web.service.UserService;
import com.web.user.User;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        try {
            String userId = loginRequest.get("userId");
            String userPassword = loginRequest.get("userPassword");
            User user = userService.findByUserIdAndUserPassword(userId, userPassword);
            if (user != null) {
                session.setAttribute("userId", user.getUserId());
                return ResponseEntity.ok(Map.of("message", "Login success"));
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid user ID format"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok(Map.of("message", "Register success"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserId(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(Map.of("message", "Logout success"));
    }
    // 사용자 정보 수정 엔드포인트 추가
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
    	User updatedUser = userService.updateUser(user);
    	return ResponseEntity.ok(updatedUser);
    }
    
}
