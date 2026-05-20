package com.rituals.api.controller;

import com.rituals.api.model.User;
import com.rituals.api.repository.UserRepository;
import com.rituals.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*") // Allows your React app to talk to this endpoint
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User request) {

        // 1. Check if the user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already in use."));
        }

        // 2. The Cryptographic Shredder (Hash the password)
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // BOOM. Secured.
        newUser.setRole("ROLE_USER");

        // 3. Save to MongoDB
        userRepository.save(newUser);

        // 4. Generate the JWT Passport
        String jwtToken = jwtService.generateToken(newUser.getEmail());

        // 5. Send the passport back to React
        return ResponseEntity.ok(Map.of(
                "message", "Registration successful",
                "token", jwtToken
        ));
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User request) {
        try {
            // 1. Spring Security verifies the email and the BCrypt hashed password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // 2. If it succeeds, generate a fresh passport
            String jwtToken = jwtService.generateToken(request.getEmail());

            // 3. Send it back
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", jwtToken
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }
    }
}