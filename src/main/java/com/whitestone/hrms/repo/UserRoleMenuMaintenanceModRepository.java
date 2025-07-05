package com.whitestone.hrms.repo;
import com.whitestone.entity.UserRoleMenuMaintenanceMod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMenuMaintenanceModRepository extends JpaRepository<UserRoleMenuMaintenanceMod, String> {
	//You can define custom query methods here if needed.
}