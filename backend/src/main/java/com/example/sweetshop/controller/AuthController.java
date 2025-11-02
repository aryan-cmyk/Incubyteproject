package com.example.sweetshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sweetshop.dto.AuthRequest;
import com.example.sweetshop.dto.AuthResponse;
import com.example.sweetshop.entity.User;
import com.example.sweetshop.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest req, @RequestParam(defaultValue = "false") boolean admin) {
    User u = authService.register(req.getEmail(), req.getPassword(), admin);
    return ResponseEntity.status(201).body(new AuthResponse(u.getEmail(), null));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
    String token = authService.login(req);
    return ResponseEntity.ok(new AuthResponse(req.getEmail(), token));
  }
}
