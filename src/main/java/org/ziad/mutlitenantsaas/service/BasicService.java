package org.ziad.mutlitenantsaas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BasicService<I , O> {

    void create(final I request);

    void update(final String id , final I request);

    O findById(final String id);

    void delete(final String id);

    Page<O> findAll(final Pageable pageable);
}
