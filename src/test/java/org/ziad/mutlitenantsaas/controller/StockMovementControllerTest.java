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
import org.ziad.mutlitenantsaas.dto.request.StockMovementRequest;
import org.ziad.mutlitenantsaas.dto.response.StockMovementResponse;
import org.ziad.mutlitenantsaas.entity.TypeMovement;
import org.ziad.mutlitenantsaas.service.StockMovementService;

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

class StockMovementControllerTest {

    private MockMvc mockMvc;

    private StockMovementService stockMovementService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.stockMovementService = mock(StockMovementService.class);
        this.objectMapper = new ObjectMapper();

        StockMovementController controller = new StockMovementController(this.stockMovementService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /v1/stocks returns 201 when payload is valid")
    void createStockMovementReturnsCreatedWhenPayloadIsValid() throws Exception {
        StockMovementRequest request = StockMovementRequest.builder()
                .typeMovement(TypeMovement.IN)
                .quantity(25)
                .comment("Received from supplier")
                .productId("prod-1")
                .build();
        doNothing().when(this.stockMovementService).create(any(StockMovementRequest.class));

        this.mockMvc.perform(post("/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /v1/stocks returns 400 when payload is invalid")
    void createStockMovementReturnsBadRequestWhenPayloadIsInvalid() throws Exception {
        StockMovementRequest request = StockMovementRequest.builder()
                .typeMovement(null)
                .quantity(0)
                .productId(" ")
                .build();

        this.mockMvc.perform(post("/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.typeMovement").exists())
                .andExpect(jsonPath("$.errors.quantity").exists())
                .andExpect(jsonPath("$.errors.productId").exists());
    }

    @Test
    @DisplayName("GET /v1/stocks/product/{productId} returns paginated stock movements")
    void getStockMovementsByProductReturnsPage() throws Exception {
        StockMovementResponse response = StockMovementResponse.builder()
                .id("mvt-1")
                .typeMovement(TypeMovement.IN)
                .quantity(25)
                .comment("Received")
                .build();
        when(this.stockMovementService.findAllByProductId(eq("prod-1"), any()))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1));

        this.mockMvc.perform(get("/v1/stocks/product/{productId}", "prod-1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("mvt-1"))
                .andExpect(jsonPath("$.content[0].typeMovement").value("IN"));
    }
}

