package com.whitestone.hrms.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.UserRoleMaintenance;

@Repository
public interface UserRoleMaintenanceRepository extends JpaRepository<UserRoleMaintenance, String> {
	//You can define custom query methods here if needed.
	 Optional<UserRoleMaintenance> findByRoleid(String roleid);

	 Optional<UserRoleMaintenance> findByRolenameIgnoreCase(String rolename);

}