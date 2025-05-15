package hr.fer.ppks.auctionbackend.controller;

import hr.fer.ppks.auctionbackend.dto.AuthResponse;
import hr.fer.ppks.auctionbackend.dto.LoginRequest;
import hr.fer.ppks.auctionbackend.dto.RegisterRequest;
import hr.fer.ppks.auctionbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}