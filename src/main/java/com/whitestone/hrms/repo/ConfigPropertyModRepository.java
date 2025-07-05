package com.whitestone.hrms.repo;
import com.whitestone.entity.ConfigPropertyMod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigPropertyModRepository extends JpaRepository<ConfigPropertyMod, String> {
	//You can define custom query methods here if needed.
}