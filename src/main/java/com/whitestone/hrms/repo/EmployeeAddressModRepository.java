package com.whitestone.hrms.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeAddressMod;

@Repository
public interface EmployeeAddressModRepository extends JpaRepository<EmployeeAddressMod, String> {
	Optional<EmployeeAddressMod> findByUserid(Long userid);
}