package org.ziad.mutlitenantsaas.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ziad.mutlitenantsaas.dto.request.CategoryRequest;
import org.ziad.mutlitenantsaas.dto.response.CategoryResponse;
import org.ziad.mutlitenantsaas.entity.Category;
import org.ziad.mutlitenantsaas.exception.AlreadyExistsException;
import org.ziad.mutlitenantsaas.mapper.CategoryMapper;
import org.ziad.mutlitenantsaas.repositories.CategoryRepository;
import org.ziad.mutlitenantsaas.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public void create(CategoryRequest request) {
        checkIfCategoryExistsByName(request.getName());

        final Category category = categoryMapper.toEntity(request);

        categoryRepository.save(category);
    }


    @Override
    public void update(String id, CategoryRequest request) {

        final Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isEmpty()) {
            log.debug("Category with id {} not found", id);
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }

        final Category category = existingCategory.get();

        if (!category.getName().equalsIgnoreCase(request.getName())) {
            checkIfCategoryExistsByName(request.getName());
        }
        final Category categoryToUpdate = categoryMapper.toEntity(request);
        categoryToUpdate.setId(id);
        categoryRepository.save(categoryToUpdate);

    }

    @Override
    public CategoryResponse findById(String id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> {
                    log.debug("Category with id {} not found", id);
                    return new EntityNotFoundException("Category with id " + id + " not found");
                });
    }

    @Override
    public void delete(String id) {
        final Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            log.debug("Category with id {} not found", id);
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.delete(existingCategory.get());
    }

    @Override
    public Page<CategoryResponse> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    private void checkIfCategoryExistsByName(String name) {

        final Optional<Category> category = categoryRepository.findByNameIgnoreCase(name);

        if (category.isPresent()) {
            log.debug("Category with name {} already exists", name);
            throw new AlreadyExistsException("Category with name " + name + " already exists");
        }
    }
}
