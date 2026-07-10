package org.ziad.mutlitenantsaas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Generic CRUD service contract for tenant-scoped domain objects.
 *
 * <p>All tenant-scoped resource services ({@code CategoryService}, {@code ProductService},
 * {@code StockMovementService}) implement this interface to ensure a consistent API
 * surface across the application.
 *
 * @param <I> the request DTO type used for create and update operations
 * @param <O> the response DTO type returned by read operations
 */
public interface BasicService<I, O> {

    /**
     * Creates a new resource from the given request.
     *
     * @param request the validated creation payload
     */
    void create(final I request);

    /**
     * Updates an existing resource identified by {@code id}.
     *
     * @param id      UUID of the resource to update
     * @param request the validated update payload
     */
    void update(final String id, final I request);

    /**
     * Retrieves a single resource by its UUID.
     *
     * @param id UUID of the resource
     * @return the response DTO for the requested resource
     * @throws jakarta.persistence.EntityNotFoundException if no resource with this ID exists
     */
    O findById(final String id);

    /**
     * Deletes the resource identified by {@code id}.
     *
     * @param id UUID of the resource to delete
     * @throws jakarta.persistence.EntityNotFoundException if no resource with this ID exists
     */
    void delete(final String id);

    /**
     * Returns a paginated list of all resources visible to the current tenant.
     *
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of response DTOs
     */
    Page<O> findAll(final Pageable pageable);
}
