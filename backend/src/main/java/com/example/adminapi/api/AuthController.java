package com.example.adminapi.api;

import com.example.adminapi.dto.AuthResponse;
import com.example.adminapi.dto.LoginRequest;
import com.example.adminapi.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    String username = authentication.getName();
    String role = authentication.getAuthorities().stream()
      .findFirst()
      .map(a -> a.getAuthority().replace("ROLE_", ""))
      .orElse("");

    String token = jwtService.generateToken(username, role);

    return ResponseEntity.ok(new AuthResponse(token, role));
  }
}
