package org.ziad.mutlitenantsaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ziad.mutlitenantsaas.entity.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByReferenceIgnoreCase(String reference);
}