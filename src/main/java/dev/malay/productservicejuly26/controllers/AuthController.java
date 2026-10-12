package dev.malay.productservicejuly26.controllers;

import dev.malay.productservicejuly26.dtos.*;
import dev.malay.productservicejuly26.models.*;
import dev.malay.productservicejuly26.repositories.UserRepository;
import dev.malay.productservicejuly26.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository users; private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager; private final JwtService jwt;
    public AuthController(UserRepository users, PasswordEncoder encoder,
                          AuthenticationManager authenticationManager, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.authenticationManager = authenticationManager; this.jwt = jwt;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (users.existsByUsername(request.getUsername()) || users.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("Username or email is already registered");
        User user = new User(); user.setUsername(request.getUsername()); user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword())); user.setRole(Role.USER); users.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response(user));
    }
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        return response(users.findByUsername(request.getUsername()).orElseThrow());
    }
    private AuthResponse response(User user) {
        return new AuthResponse(jwt.issue(user), "Bearer", jwt.expirationMs() / 1000);
    }
}
