package com.whitestone.hrms.repo;
import com.whitestone.entity.ConfigProperty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigPropertyRepository extends JpaRepository<ConfigProperty, String> {
	//You can define custom query methods here if needed.
}