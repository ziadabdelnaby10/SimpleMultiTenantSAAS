package org.ziad.mutlitenantsaas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.ziad.mutlitenantsaas.aspect.GlobalExceptionHandler;
import org.ziad.mutlitenantsaas.dto.request.UserRequest;
import org.ziad.mutlitenantsaas.dto.response.UserResponse;
import org.ziad.mutlitenantsaas.entity.UserRole;
import org.ziad.mutlitenantsaas.service.UserService;
import org.ziad.mutlitenantsaas.support.TestRequestFactory;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.userService = mock(UserService.class);
        this.objectMapper = new ObjectMapper();

        UserController controller = new UserController(this.userService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /v1/users returns 201 when request is valid")
    void createUserReturnsCreatedWhenPayloadIsValid() throws Exception {
        UserRequest request = TestRequestFactory.validUserRequest();
        doNothing().when(this.userService).createUser(any(UserRequest.class));

        this.mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /v1/users returns 400 when payload is invalid")
    void createUserReturnsBadRequestWhenPayloadIsInvalid() throws Exception {
        UserRequest request = UserRequest.builder()
                .username("a")
                .email("bad-email")
                .password("123")
                .firstName("")
                .lastName("")
                .role(null)
                .build();

        this.mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.username").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.password").exists())
                .andExpect(jsonPath("$.errors.role").exists());
    }

    @Test
    @DisplayName("GET /v1/users returns paginated users")
    void getUsersReturnsPage() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .id("user-1")
                .username("jane.smith")
                .email("jane.smith@acme.com")
                .firstName("Jane")
                .lastName("Smith")
                .role(UserRole.ROLE_USER)
                .build();
        when(this.userService.getAllUsers(any()))
                .thenReturn(new PageImpl<>(List.of(userResponse), PageRequest.of(0, 20), 1));

        this.mockMvc.perform(get("/v1/users")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "username,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("user-1"))
                .andExpect(jsonPath("$.content[0].username").value("jane.smith"));
    }

    @Test
    @DisplayName("GET /v1/users/{userId} returns 200 for valid user ID")
    void getUserByIdReturnsOk() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .id("user-1")
                .username("jane.smith")
                .email("jane.smith@acme.com")
                .firstName("Jane")
                .lastName("Smith")
                .role(UserRole.ROLE_USER)
                .build();
        when(this.userService.getUserById(eq("user-1"))).thenReturn(userResponse);

        this.mockMvc.perform(get("/v1/users/{userId}", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-1"));
    }
}


