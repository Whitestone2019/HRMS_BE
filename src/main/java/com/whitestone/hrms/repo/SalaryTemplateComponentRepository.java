package com.whitestone.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.SalaryTemplateComponent;

public interface SalaryTemplateComponentRepository extends JpaRepository<SalaryTemplateComponent, Long> {
    List<SalaryTemplateComponent> findBySalaryTemplate_TemplateId(Long templateId);  // Add this method if needed
}
