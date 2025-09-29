package com.whitestone.hrms.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeProjectHistory;

@Repository
public interface EmployeeProjectHistoryRepository extends JpaRepository<EmployeeProjectHistory, Long> {
    List<EmployeeProjectHistory> findByEmpIdInAndEntityCreFlgIn(List<String> empIds, List<String> entityCreFlgs);
}