package com.example.sweetshop;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.sweetshop.repository.UserRepository;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setup() { userRepository.deleteAll(); }

  @Test
  void registerCreatesUser() throws Exception {
    String body = """
      {"email":"test@example.com","password":"pass123"}
      """;

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value("test@example.com"));
  }

  @Test
  void loginReturnsJwt() throws Exception {
    // register first
    String reg = """
      {"email":"t2@example.com","password":"pwd"}
      """;
    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(reg)).andExpect(status().isCreated());

    String login = """
      {"email":"t2@example.com","password":"pwd"}
      """;
    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(login))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token", notNullValue()));
  }
}
