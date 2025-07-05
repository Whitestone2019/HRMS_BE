package com.whitestone.hrms.repo;
import com.whitestone.entity.UserRoleMaintenanceMod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMaintenanceModRepository extends JpaRepository<UserRoleMaintenanceMod, String> {
	//You can define custom query methods here if needed.
}