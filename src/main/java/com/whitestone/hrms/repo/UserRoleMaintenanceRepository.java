package com.whitestone.hrms.repo;

import com.whitestone.entity.UserRoleMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleMaintenanceRepository extends JpaRepository<UserRoleMaintenance, String> {
    
    // Find by role ID
    Optional<UserRoleMaintenance> findByRoleid(String roleid);
    
    // Find by role name (case insensitive)
    Optional<UserRoleMaintenance> findByRolenameIgnoreCase(String rolename);
    
    // Find all active roles (where DEL_FLG = '0')
    List<UserRoleMaintenance> findByDelflg(String delflg);
    
    // Find by status and active
    List<UserRoleMaintenance> findByStatusAndDelflg(String status, String delflg);
    
    // Check if role ID exists
    boolean existsByRoleid(String roleid);
    
    // Check if role name exists and is active
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserRoleMaintenance u WHERE LOWER(u.rolename) = LOWER(:rolename) AND u.delflg = '0'")
    boolean existsActiveByRolename(@Param("rolename") String rolename);
    
    // Find active by role ID
    @Query("SELECT u FROM UserRoleMaintenance u WHERE u.roleid = :roleId AND u.delflg = '0'")
    Optional<UserRoleMaintenance> findActiveByRoleId(@Param("roleId") String roleId);
}