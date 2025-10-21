package com.whitestone.hrms.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.whitestone.entity.EmployeeLeaveModTbl;

@Repository

public interface EmployeeLeaveModTblRepository extends JpaRepository<EmployeeLeaveModTbl, Long> {

	@Query("SELECT SUM(e.noofdays) FROM EmployeeLeaveModTbl e WHERE e.leavetype = :leavetype AND e.empid = :empid AND e.status = 'Approved'")

	Long countByLeavetypeAndEmpid(@Param("leavetype") String leavetype, @Param("empid") String empid);

	List<EmployeeLeaveModTbl> findByEmpidIn(List<String> empids);

	@Query("SELECT COUNT(e) FROM EmployeeLeaveModTbl e WHERE e.empid = :empId AND YEAR(e.startdate) = :year AND MONTH(e.startdate) < :currentMonth")
	long countLeaveTakenBefore(@Param("empId") String empId, @Param("year") int year,
			@Param("currentMonth") int currentMonth);
	
	List<EmployeeLeaveModTbl> findByEmpidAndStartdateBetweenAndStatusIn(
            String empid,
            Date startdate,
            Date enddate,
            List<String> statuses
    );

}
