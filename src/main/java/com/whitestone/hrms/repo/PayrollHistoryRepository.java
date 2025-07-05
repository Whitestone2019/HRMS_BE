package com.whitestone.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.PayrollHistory;

@Repository
public interface PayrollHistoryRepository extends JpaRepository<PayrollHistory, Long> {
}
