package com.whitestone.hrms.repo;
import com.whitestone.entity.UserRoleMenuMaintenance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMenuMaintenanceRepository extends JpaRepository<UserRoleMenuMaintenance, String> {
	//You can define custom query methods here if needed.
}