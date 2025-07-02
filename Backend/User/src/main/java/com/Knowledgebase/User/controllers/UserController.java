package com.Knowledgebase.User.controllers;

import com.Knowledgebase.User.dtos.LoginDto;
import com.Knowledgebase.User.entities.User;
import com.Knowledgebase.User.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody LoginDto loginDto){
        User user = userService.registerUser(loginDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto){
        String token = userService.loginUser(loginDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        String token = userService.forgotPassword(email);
        if (token != null) {
            return ResponseEntity.ok("Password reset token: " + token);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        boolean success = userService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }

}
