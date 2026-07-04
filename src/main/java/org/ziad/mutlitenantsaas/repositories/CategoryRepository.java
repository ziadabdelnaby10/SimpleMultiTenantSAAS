package org.ziad.mutlitenantsaas.repositories;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.ziad.mutlitenantsaas.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @QueryHints(value = {
            @QueryHint(name = "org.hibernate.readOnly", value = "true"),
    })
    Optional<Category> findByNameIgnoreCase(String name);
}