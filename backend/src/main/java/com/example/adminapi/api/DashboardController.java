package com.example.adminapi.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public ResponseEntity<Map<String, Object>> overview() {
    return ResponseEntity.ok(Map.of(
      "status", "ok",
      "message", "Dashboard accessible",
      "role", "ADMIN or USER"
    ));
  }
}
