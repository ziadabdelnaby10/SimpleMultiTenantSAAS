package org.ziad.mutlitenantsaas.service;

import org.ziad.mutlitenantsaas.entity.Tenant;

public interface ProvisioningService {

    void provision(Tenant tenant);
}
