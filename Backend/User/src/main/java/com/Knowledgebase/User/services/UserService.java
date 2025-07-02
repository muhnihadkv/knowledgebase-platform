package com.Knowledgebase.User.services;

import com.Knowledgebase.User.dtos.LoginDto;
import com.Knowledgebase.User.entities.Roles;
import com.Knowledgebase.User.entities.User;
import com.Knowledgebase.User.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User registerUser(LoginDto loginDto) {
        User user = new User();
        user.setEmail(loginDto.getEmail());
        user.setPassword(passwordEncoder.encode(loginDto.getPassword()));
        user.setName(loginDto.getName());
        user.setRole(Roles.USER);
        userRepository.save(user);
        return user;
    }

    public String loginUser(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginDto.getEmail());
        return jwtService.generateToken(user);
    }

    public int getUserId(String token){
        return jwtService.extractUserId(token);
    }

    public String forgotPassword(String email) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Generate secure token
            String token = UUID.randomUUID().toString();
            long expiry = Instant.now().plusSeconds(60 * 60).toEpochMilli(); // 1 hour expiry

            user.setResetToken(token);
            user.setResetTokenExpiry(expiry);
            userRepository.save(user);

            // Send reset token via email (ideally a clickable link)
            String resetLink = "https://localhost:8080/reset-password?token=" + token;
            emailService.sendSimpleMessage(
                    user.getEmail(),
                    "Password Reset Request",
                    "Click the link to reset your password: " + resetLink + "\n\nThis link expires in 1 hour."
            );
        }

        // Do not reveal if user/email exists for security
        return "If this email is registered, you will receive a password reset link.";
    }


    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getResetToken()))
                .findFirst();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getResetTokenExpiry() != null && user.getResetTokenExpiry() > Instant.now().toEpochMilli()) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetToken(null);
                user.setResetTokenExpiry(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
