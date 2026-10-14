package com.smartstay.repository;
import com.smartstay.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TenantRepository extends JpaRepository<Tenant, Long> {}
