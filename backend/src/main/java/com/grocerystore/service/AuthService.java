package com.grocerystore.service;
import com.grocerystore.dto.*;
import com.grocerystore.entity.User;
import com.grocerystore.exception.ResourceAlreadyExistsException;
import com.grocerystore.repository.UserRepository;
import com.grocerystore.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new ResourceAlreadyExistsException("Email already registered: " + req.getEmail());

        var user = User.builder()
            .firstName(req.getFirstName())
            .lastName(req.getLastName())
            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .phone(req.getPhone())
            .role(User.Role.USER)
            .enabled(true)
            .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder()
            .token(token).email(user.getEmail())
            .firstName(user.getFirstName()).lastName(user.getLastName())
            .role(user.getRole().name()).build();
    }

    public AuthResponse login(AuthRequest req) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        var user = userRepository.findByEmail(req.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder()
            .token(token).email(user.getEmail())
            .firstName(user.getFirstName()).lastName(user.getLastName())
            .role(user.getRole().name()).build();
    }
}
