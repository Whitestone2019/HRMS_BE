package com.whitestone.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.LocationAllowance;

@Repository
public interface LocationAllowanceRepository extends JpaRepository<LocationAllowance, Long> {
    List<LocationAllowance> findByLocationName(String locationName);
}
