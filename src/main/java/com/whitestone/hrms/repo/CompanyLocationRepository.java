package com.whitestone.hrms.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.CompanyLocation;

@Repository
public interface CompanyLocationRepository extends JpaRepository<CompanyLocation, Long> {
    // You can add custom query methods here if needed
}