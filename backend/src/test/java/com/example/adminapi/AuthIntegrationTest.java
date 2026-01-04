package com.example.adminapi;

import com.example.adminapi.domain.Role;
import com.example.adminapi.domain.User;
import com.example.adminapi.domain.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private String userToken;
  private String adminToken;

  @BeforeEach
  void setup() throws Exception {
    // ensure a USER exists for role-based checks
    userRepository.findByUsername("basicuser").orElseGet(() ->
      userRepository.save(User.builder()
        .username("basicuser")
        .email("user@example.com")
        .password(passwordEncoder.encode("user123"))
        .role(Role.USER)
        .build())
    );

    adminToken = obtainToken("admin", "admin123");
    userToken = obtainToken("basicuser", "user123");
  }

  @Test
  void login_shouldReturnJwtToken() {
    assertThat(adminToken).isNotBlank();
  }

  @Test
  void dashboard_shouldRequireAuthentication() throws Exception {
    mockMvc.perform(get("/api/dashboard"))
      .andExpect(status().isForbidden());
  }

  @Test
  void dashboard_shouldAllowAuthenticatedUser() throws Exception {
    mockMvc.perform(get("/api/dashboard")
        .header("Authorization", "Bearer " + userToken))
      .andExpect(status().isOk());
  }

  @Test
  void usersEndpoint_shouldAllowAdmin() throws Exception {
    mockMvc.perform(get("/api/users")
        .header("Authorization", "Bearer " + adminToken))
      .andExpect(status().isOk());
  }

  @Test
  void usersEndpoint_shouldForbidNonAdmin() throws Exception {
    mockMvc.perform(get("/api/users")
        .header("Authorization", "Bearer " + userToken))
      .andExpect(status().isForbidden());
  }

  private String obtainToken(String username, String password) throws Exception {
    ResultActions result = mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
      .andExpect(status().isOk());

    String body = result.andReturn().getResponse().getContentAsString();
    JsonNode json = objectMapper.readTree(body);
    return json.get("token").asText();
  }
}
