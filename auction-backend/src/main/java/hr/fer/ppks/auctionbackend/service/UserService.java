package hr.fer.ppks.auctionbackend.service;

import hr.fer.ppks.auctionbackend.dto.AuthResponse;
import hr.fer.ppks.auctionbackend.dto.LoginRequest;
import hr.fer.ppks.auctionbackend.dto.RegisterRequest;
import hr.fer.ppks.auctionbackend.model.User;
import hr.fer.ppks.auctionbackend.repository.UserRepository;
import hr.fer.ppks.auctionbackend.security.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if username is already taken
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        // Create new user
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
        );

        // Make sure to save the user before generating the token
        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenUtil.generateToken(user);

        return new AuthResponse(token, user.getUsername(), user.getId());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Ensure user exists in database
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate JWT token
            String token = jwtTokenUtil.generateToken(user);

            // Debug - logging the token for verification
            System.out.println("Generated token for user " + user.getUsername() + ": " + token.substring(0, 20) + "...");

            return new AuthResponse(token, user.getUsername(), user.getId());
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Invalid username or password");
        }
    }
}