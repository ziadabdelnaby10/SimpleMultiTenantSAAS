package org.ziad.mutlitenantsaas.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ziad.mutlitenantsaas.dto.request.StockMovementRequest;
import org.ziad.mutlitenantsaas.dto.response.StockMovementResponse;
import org.ziad.mutlitenantsaas.entity.StockMovement;
import org.ziad.mutlitenantsaas.mapper.StockMovementMapper;
import org.ziad.mutlitenantsaas.repositories.StockMovementRepository;
import org.ziad.mutlitenantsaas.service.ProductService;
import org.ziad.mutlitenantsaas.service.StockMovementService;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final StockMovementMapper stockMovementMapper;
    private final ProductService productService;

    @Override
    public void create(StockMovementRequest request) {
        // check if product exists
        checkIfProductExistsById(request.getProductId());

        final StockMovement entity = this.stockMovementMapper.toEntity(request);
        entity.setDateMovement(Instant.now());
        this.stockMovementRepository.save(entity);
    }

    @Override
    public void update(String id, StockMovementRequest request) {
        final Optional<StockMovement> stockMvt = this.stockMovementRepository.findById(id);
        if (stockMvt.isEmpty()) {
            log.debug("StockMvt does not exist");
            throw new EntityNotFoundException("StockMvt does not exist");
        }

        // check if product exists
        checkIfProductExistsById(request.getProductId());

        final StockMovement stockMvtToUpdate = this.stockMovementMapper.toEntity(request);
        stockMvtToUpdate.setDateMovement(Instant.now());
        stockMvtToUpdate.setId(id);
        this.stockMovementRepository.save(stockMvtToUpdate);
    }

    @Override
    public StockMovementResponse findById(String id) {
        return this.stockMovementRepository.findById(id)
                .map(this.stockMovementMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("StockMvt does not exist"));
    }

    @Override
    public void delete(String id) {
        final StockMovement stockMvt = this.stockMovementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockMvt does not exist"));
        this.stockMovementRepository.delete(stockMvt);
    }

    @Override
    public Page<StockMovementResponse> findAll(Pageable pageable) {
        return stockMovementRepository.findAll(pageable).map(stockMovementMapper::toResponse);
    }

    private void checkIfProductExistsById(final String productId) {
        if (productService.existsById(productId)) {
            throw new EntityNotFoundException("Product does not exist");
        }
    }
}
