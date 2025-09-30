package com.whitestone.hrms.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.usermaintenance;
@EnableJpaRepositories(basePackages = "com.whitestone.hrms.repo")

@Repository
public interface usermaintenanceRepository extends JpaRepository<usermaintenance, String> {
	
	// Check both empid and userid with Active status
    @Query("SELECT u FROM usermaintenance u WHERE (u.empid = :empid OR u.userid = :empid) AND u.status = 'Active'")
    Optional<usermaintenance> findByEmpIdOrUserId(@Param("empid") String empid);

    // Find all employees reporting to given manager, Active only
    @Query("SELECT u FROM usermaintenance u WHERE u.repoteTo = :empId AND u.status = 'Active'")
    List<usermaintenance> findByRepoteToCustom(@Param("empId") String empId);

    // Alternative method (you can remove one to avoid duplication)
    @Query("SELECT u FROM usermaintenance u WHERE u.repoteTo = :repoteTo AND u.status = 'Active'")
    List<usermaintenance> findByRepoteTo(@Param("repoteTo") String repoteTo);

    // Find by empid and check Active
    @Query("SELECT u FROM usermaintenance u WHERE u.empid = :empid AND u.status = 'Active'")
    usermaintenance findByEmpid(@Param("empid") String empid);

    // Find multiple empIds, Active only
    @Query("SELECT u FROM usermaintenance u WHERE u.empid IN :empIds AND u.status = 'Active'")
    List<usermaintenance> findByEmpidIn(@Param("empIds") List<String> empIds);
    
    boolean existsByEmpid(String empid);

}