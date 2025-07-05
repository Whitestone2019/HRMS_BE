package com.whitestone.hrms.repo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeLeaveMasterTbl;
 
@Repository

public interface EmployeeLeaveMasterTblRepository extends JpaRepository<EmployeeLeaveMasterTbl, Long> {

    @Query("SELECT COUNT(e) FROM EmployeeLeaveMasterTbl e WHERE e.leaveType = :leaveType AND e.empid = :empid")

    Long countByLeaveTypeAndEmpid(@Param("leaveType") String leaveType, @Param("empid") String empid);
 
   // EmployeeLeaveMasterTbl findByEmpid(String empid); // Correct field name
 
	EmployeeLeaveMasterTbl findByEmpid(String empid);
	
	 EmployeeLeaveMasterTbl findByEmpidAndLeaveType(String empid, String leaveType);


	  List<EmployeeLeaveMasterTbl> findByEntitycreflg(String entitycreflg);
 
	List<EmployeeLeaveMasterTbl> findAllByEntitycreflgIn(List<String> of);
 
	List<EmployeeLeaveMasterTbl> findAllByDelflg(String string);

	EmployeeLeaveMasterTbl findByEmpidAndLeaveReason(String empid, String leaveReason);
	
	 List<EmployeeLeaveMasterTbl> findByEmpidInAndEntitycreflgIn(List<String> empIds, List<String> entityFlags);
	 
	 @Query(value = "SELECT COUNT(*) FROM EMPLOYEE_LEAVE_MASTER_TBL WHERE EMP_ID = ?1 AND TRUNC(START_DATE) = TRUNC(?2)", nativeQuery = true)
	 int countByEmpidAndStartDate(String empid, Timestamp startDate);




}

 