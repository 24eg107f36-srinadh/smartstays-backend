package com.smartstay.controller;

import com.smartstay.entity.UserAccount;
import com.smartstay.repository.UserAccountRepository;
import com.smartstay.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwt;
    private final PasswordEncoder encoder;
    private final UserAccountRepository users;
    private final String ownerUsername;
    private final String ownerPasswordHash;

    public AuthController(JwtService jwt, PasswordEncoder encoder, UserAccountRepository users,
            @Value("${app.owner.username}") String ownerUsername,
            @Value("${app.owner.password}") String ownerPassword) {
        this.jwt = jwt; this.encoder = encoder; this.users = users;
        this.ownerUsername = ownerUsername;
        this.ownerPasswordHash = encoder.encode(ownerPassword);
    }

    public record LoginRequest(String username, String password) {}

    @PostMapping("/register")
    public Map<String,String> register(@RequestBody LoginRequest req) {
        if (req == null || req.username() == null || req.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password are required");
        }
        String username = req.username().trim();
        String password = req.password();
        if (!username.matches("[A-Za-z0-9_.-]{3,80}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be 3-80 characters and use letters, numbers, dot, underscore or hyphen");
        }
        if (password.length() < 8 || password.length() > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be between 8 and 72 characters");
        }
        if (users.existsByUsername(username) || ownerUsername.equals(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "That username is already registered");
        }
        users.save(new UserAccount(username, encoder.encode(password)));
        return Map.of("token", jwt.create(username), "username", username);
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody LoginRequest req) {
        if (req == null || req.username() == null || req.password() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        String username = req.username().trim();
        UserAccount user = users.findByUsername(username).orElse(null);
        if (user != null && encoder.matches(req.password(), user.getPasswordHash())) {
            return Map.of("token", jwt.create(user.getUsername()), "username", user.getUsername());
        }
        // Keep the starter owner account available for local setup and recovery.
        if (ownerUsername.equals(username) && encoder.matches(req.password(), ownerPasswordHash)) {
            return Map.of("token", jwt.create(ownerUsername), "username", ownerUsername);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }
}
