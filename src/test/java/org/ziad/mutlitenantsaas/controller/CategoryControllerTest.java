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
import org.ziad.mutlitenantsaas.dto.request.CategoryRequest;
import org.ziad.mutlitenantsaas.dto.response.CategoryResponse;
import org.ziad.mutlitenantsaas.service.CategoryService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest {

    private MockMvc mockMvc;

    private CategoryService categoryService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.categoryService = mock(CategoryService.class);
        this.objectMapper = new ObjectMapper();

        CategoryController controller = new CategoryController(this.categoryService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /v1/categories returns 201 when payload is valid")
    void createCategoryReturnsCreatedWhenPayloadIsValid() throws Exception {
        CategoryRequest request = CategoryRequest.builder().name("Electronics").description("Devices").build();
        doNothing().when(this.categoryService).create(any(CategoryRequest.class));

        this.mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /v1/categories returns 400 when payload is invalid")
    void createCategoryReturnsBadRequestWhenPayloadIsInvalid() throws Exception {
        CategoryRequest request = CategoryRequest.builder().name(" ").description("d").build();

        this.mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    @DisplayName("GET /v1/categories returns paginated categories")
    void getCategoriesReturnsPage() throws Exception {
        CategoryResponse response = CategoryResponse.builder().id("cat-1").name("Electronics").description("Devices").build();
        when(this.categoryService.findAll(any()))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1));

        this.mockMvc.perform(get("/v1/categories").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("cat-1"))
                .andExpect(jsonPath("$.content[0].name").value("Electronics"));
    }

    @Test
    @DisplayName("GET /v1/categories/{categoryId} returns 200 when category exists")
    void getCategoryByIdReturnsOk() throws Exception {
        CategoryResponse response = CategoryResponse.builder().id("cat-1").name("Electronics").description("Devices").build();
        when(this.categoryService.findById("cat-1")).thenReturn(response);

        this.mockMvc.perform(get("/v1/categories/{categoryId}", "cat-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cat-1"));
    }
}

