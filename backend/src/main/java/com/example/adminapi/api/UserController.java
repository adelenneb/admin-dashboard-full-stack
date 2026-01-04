package com.example.adminapi.api;

import com.example.adminapi.domain.Role;
import com.example.adminapi.domain.User;
import com.example.adminapi.domain.UserRepository;
import com.example.adminapi.dto.UserDto;
import com.example.adminapi.dto.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> list() {
    List<UserDto> users = userRepository.findAll().stream()
      .map(this::toDto)
      .toList();
    return ResponseEntity.ok(users);
  }

  @PostMapping
  public ResponseEntity<UserDto> create(@Valid @RequestBody UserRequest request) {
    validateUnique(request, null);

    User user = User.builder()
      .username(request.username())
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .role(Role.valueOf(request.role()))
      .build();

    User saved = userRepository.save(user);
    return ResponseEntity.ok(toDto(saved));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
    User existing = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    validateUnique(request, id);

    existing.setUsername(request.username());
    existing.setEmail(request.email());
    if (request.password() != null && !request.password().isBlank()) {
      existing.setPassword(passwordEncoder.encode(request.password()));
    }
    existing.setRole(Role.valueOf(request.role()));

    User saved = userRepository.save(existing);
    return ResponseEntity.ok(toDto(saved));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  private UserDto toDto(User user) {
    return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
  }

  private void validateUnique(UserRequest request, Long currentId) {
    userRepository.findByUsername(request.username()).ifPresent(u -> {
      if (currentId == null || !u.getId().equals(currentId)) {
        throw new RuntimeException("Username already exists");
      }
    });
    userRepository.findByEmail(request.email()).ifPresent(u -> {
      if (currentId == null || !u.getId().equals(currentId)) {
        throw new RuntimeException("Email already exists");
      }
    });
  }
}
