// FingerprintRepository.java
package com.whitestone.hrms.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.Fingerprint;

@Repository
public interface FingerprintRepository extends JpaRepository<Fingerprint, Long> {
    Optional<Fingerprint> findByEmployeeId(String employeeId);
    boolean existsByEmployeeId(String employeeId);
    void deleteByEmployeeId(String employeeId);
}