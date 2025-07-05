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
	
	@Query("SELECT u FROM usermaintenance u WHERE u.empid = :empid OR u.userid = :empid")
	Optional<usermaintenance> findByEmpIdOrUserId(@Param("empid") String empid);

	@Query("SELECT u FROM usermaintenance u WHERE u.repoteTo = :empId")
	List<usermaintenance> findByRepoteToCustom(@Param("empId") String empId);

	@Query("SELECT u FROM usermaintenance u WHERE u.repoteTo = :repoteTo")
    List<usermaintenance> findByRepoteTo(String repoteTo);
    // You can define custom query methods here if needed.
    
    usermaintenance findByEmpid(String empid);
}