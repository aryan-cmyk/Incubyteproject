package com.example.sweetshop.service;

import com.example.sweetshop.dto.AuthRequest;
import com.example.sweetshop.entity.User;
import com.example.sweetshop.repository.UserRepository;
import com.example.sweetshop.config.JwtUtil;
import com.example.sweetshop.exception.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  public User register(String email, String password, boolean admin) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new BadRequestException("Email already registered");
    }
    User u = new User();
    u.setEmail(email);
    u.setPassword(encoder.encode(password));
    u.setRole(admin ? "ROLE_ADMIN" : "ROLE_USER");
    return userRepository.save(u);
  }

  public String login(AuthRequest req) {
    var opt = userRepository.findByEmail(req.getEmail());
    if (opt.isEmpty()) throw new BadRequestException("Invalid credentials");
    User user = opt.get();
    if (!encoder.matches(req.getPassword(), user.getPassword())) throw new BadRequestException("Invalid credentials");
    return jwtUtil.generateToken(user.getId().toString(), user.getEmail(), user.getRole());
  }
}
