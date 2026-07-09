package org.ziad.mutlitenantsaas.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ziad.mutlitenantsaas.dto.request.RegisterTenantRequest;
import org.ziad.mutlitenantsaas.dto.response.TenantResponse;
import org.ziad.mutlitenantsaas.entity.Tenant;
import org.ziad.mutlitenantsaas.entity.TenantStatus;
import org.ziad.mutlitenantsaas.event.TenantApprovedEvent;
import org.ziad.mutlitenantsaas.exception.AlreadyExistsException;
import org.ziad.mutlitenantsaas.mapper.TenantMapper;
import org.ziad.mutlitenantsaas.repositories.TenantRepository;
import org.ziad.mutlitenantsaas.service.PasswordEncryptionService;
import org.ziad.mutlitenantsaas.service.ProvisioningService;
import org.ziad.mutlitenantsaas.service.TenantService;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private final PasswordEncryptionService passwordEncryptionService;
    private final ProvisioningService provisioningService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Tenant getReferenceById(String tenantId) {
        return tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant with id " + tenantId + " not found"));
    }

    @Override
    public void registerTenant(RegisterTenantRequest request) {
        checkIfTenantAlreadyExistsForRegister(request);
        final Tenant tenant = tenantMapper.toEntity(request);
        tenant.setAdminPassword(passwordEncryptionService.encryptPassword(request.getAdminPassword()));
        tenant.setStatus(TenantStatus.PENDING);
        tenantRepository.save(tenant);
    }

    @Override
    public void approveTenant(String tenantId) {

        final Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant with id " + tenantId + " not found"));

        if (tenant.getStatus() != TenantStatus.PENDING) {
            throw new IllegalStateException("Tenant with id " + tenantId + " is not in PENDING status");
        }

        tenant.setStatus(TenantStatus.ACTIVE);

        provisioningService.provision(tenant);

        eventPublisher.publishEvent(new TenantApprovedEvent(tenant));

    }

    @Override
    public void activateTenant(String tenantId) {
        final Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant with id " + tenantId + " not found"));

        if (!(tenant.getStatus() == TenantStatus.SUSPENDED || tenant.getStatus() == TenantStatus.INACTIVE)) {
            throw new IllegalStateException("Tenant with id " + tenantId + " is not in SUSPENDED or INACTIVE status");
        }

        tenant.setStatus(TenantStatus.ACTIVE);

        tenantRepository.save(tenant);
    }

    @Override
    public void deactivateTenant(String tenantId) {
        final Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant with id " + tenantId + " not found"));

        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new IllegalStateException("Tenant with id " + tenantId + " is not in ACTIVE status");
        }

        tenant.setStatus(TenantStatus.INACTIVE);

        tenantRepository.save(tenant);
    }

    @Override
    public void suspendTenant(String tenantId) {
        final Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant with id " + tenantId + " not found"));

        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new IllegalStateException("Tenant with id " + tenantId + " is not in ACTIVE status");
        }

        tenant.setStatus(TenantStatus.SUSPENDED);

        tenantRepository.save(tenant);
    }

    @Override
    public Page<TenantResponse> findAll(Pageable pageable) {
        return tenantRepository.findAll(pageable).map(tenantMapper::toResponse);
    }

    public void checkIfTenantAlreadyExistsForRegister(RegisterTenantRequest request) {
        if (tenantRepository.existsByCompanyCode(request.getCompanyCode())) {
            throw new AlreadyExistsException("Tenant with company code " + request.getCompanyCode() + " already exists");
        }
        if (tenantRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Tenant with Email " + request.getEmail() + " already exists");
        }
    }
}
