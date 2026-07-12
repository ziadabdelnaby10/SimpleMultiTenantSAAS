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
import org.ziad.mutlitenantsaas.dto.request.ProductRequest;
import org.ziad.mutlitenantsaas.dto.response.ProductResponse;
import org.ziad.mutlitenantsaas.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private MockMvc mockMvc;

    private ProductService productService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.productService = mock(ProductService.class);
        this.objectMapper = new ObjectMapper();

        ProductController controller = new ProductController(this.productService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /v1/products returns 201 when payload is valid")
    void createProductReturnsCreatedWhenPayloadIsValid() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .name("Wireless Mouse")
                .reference("WM-001")
                .description("Ergonomic")
                .alertThreshold(10)
                .price(BigDecimal.valueOf(29.99))
                .categoryId("cat-1")
                .build();
        doNothing().when(this.productService).create(any(ProductRequest.class));

        this.mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /v1/products returns 400 when payload is invalid")
    void createProductReturnsBadRequestWhenPayloadIsInvalid() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .name("x")
                .reference("y")
                .alertThreshold(0)
                .price(BigDecimal.ZERO)
                .categoryId(" ")
                .build();

        this.mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.reference").exists())
                .andExpect(jsonPath("$.errors.alertThreshold").exists())
                .andExpect(jsonPath("$.errors.price").exists())
                .andExpect(jsonPath("$.errors.categoryId").exists());
    }

    @Test
    @DisplayName("GET /v1/products returns paginated products")
    void getProductsReturnsPage() throws Exception {
        ProductResponse response = ProductResponse.builder()
                .id("prod-1")
                .name("Wireless Mouse")
                .reference("WM-001")
                .price(BigDecimal.valueOf(29.99))
                .categoryId("cat-1")
                .build();
        when(this.productService.findAll(any()))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1));

        this.mockMvc.perform(get("/v1/products").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("prod-1"))
                .andExpect(jsonPath("$.content[0].reference").value("WM-001"));
    }
}

