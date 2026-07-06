package org.ziad.mutlitenantsaas.service;

import org.ziad.mutlitenantsaas.dto.request.ProductRequest;
import org.ziad.mutlitenantsaas.dto.response.ProductResponse;

public interface ProductService extends BasicService<ProductRequest, ProductResponse> {
    Boolean existsById(String id);
}
