package com.whitestone.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.StatutorySettings;

public interface StatutorySettingsRepository extends JpaRepository<StatutorySettings, Long> {
}