package org.ziad.mutlitenantsaas.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ziad.mutlitenantsaas.dto.request.ProductRequest;
import org.ziad.mutlitenantsaas.dto.response.ProductResponse;
import org.ziad.mutlitenantsaas.entity.Product;
import org.ziad.mutlitenantsaas.exception.AlreadyExistsException;
import org.ziad.mutlitenantsaas.mapper.ProductMapper;
import org.ziad.mutlitenantsaas.repositories.ProductRepository;
import org.ziad.mutlitenantsaas.service.CategoryService;
import org.ziad.mutlitenantsaas.service.ProductService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Override
    public void create(ProductRequest request) {
        // check if product already exists
        checkIfProductAlreadyExistsByReference(request.getReference());

        // check if category exists
        checkIfCategoryExistById(request.getCategoryId());

        final Product entity = this.productMapper.toEntity(request);
        this.productRepository.save(entity);
    }

    @Override
    public void update(String id, ProductRequest request) {
        // check if product exists
        final Optional<Product> productExists = this.productRepository.findById(id);
        if (productExists.isEmpty()) {
            log.debug("Product does not exist");
            throw new EntityNotFoundException("Product does not exist");
        }

        // check if product already exists
        if (!productExists.get().getReference().equalsIgnoreCase(request.getReference())) {
            checkIfProductAlreadyExistsByReference(request.getReference());
        }

        // check if category exists
        checkIfCategoryExistById(request.getCategoryId());

        final Product productToUpdate = this.productMapper.toEntity(request);
        productToUpdate.setId(id);
        this.productRepository.save(productToUpdate);
    }

    @Override
    public ProductResponse findById(String id) {
        return this.productRepository.findById(id)
                .map(this.productMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product does not exist"));
    }

    @Override
    public void delete(String id) {
        final Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product does not exist"));
        this.productRepository.delete(product);
    }

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(this.productMapper::toResponse);
    }

    private void checkIfProductAlreadyExistsByReference(final String reference) {
        final Optional<Product> product = this.productRepository.findByReferenceIgnoreCase(reference);
        if (product.isPresent()) {
            log.debug("Product already exists");
            throw new AlreadyExistsException("Product already exists"); // we will use custom exception later
        }
    }

    private void checkIfCategoryExistById(final String categoryId) {
        if (!categoryService.existsById(categoryId))
            throw new EntityNotFoundException("Category does not exist");
    }

    @Override
    public Boolean existsById(String id) {
        return productRepository.existsById(id);
    }
}
