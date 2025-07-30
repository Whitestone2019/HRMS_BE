package com.whitestone.hrms.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.UserMasterAttendance;

@Repository

public interface UserMasterAttendanceRepository extends JpaRepository<UserMasterAttendance, String> {

	@Query(value =
		    "SELECT " +
		    "U.ATTENDANCE_ID AS attendanceId, " +
		    "E.EMPLOYEE_NAME AS employeeName, " +
		    "COUNT(DISTINCT U.ATTENDANCE_DATE) AS distinctAttendanceCount, " +
		    "COALESCE(" +
		    "   (SELECT SUM(L.NO_OF_DAYS) " +
		    "    FROM EMPLOYEE_LEAVE_MOD_TBL_TEST L " +
		    "    WHERE L.EMP_ID = U.ATTENDANCE_ID " +
		    "    AND EXTRACT(YEAR FROM L.START_DATE) = EXTRACT(YEAR FROM (" +
		    "        SELECT MIN(U2.ATTENDANCE_DATE) " +
		    "        FROM USER_MASTER_ATTENDANCE_MOD_TBL U2 " +
		    "        WHERE U2.ATTENDANCE_ID = U.ATTENDANCE_ID)) " +
		    "    AND EXTRACT(MONTH FROM L.START_DATE) = EXTRACT(MONTH FROM (" +
		    "        SELECT MIN(U2.ATTENDANCE_DATE) " +
		    "        FROM USER_MASTER_ATTENDANCE_MOD_TBL U2 " +
		    "        WHERE U2.ATTENDANCE_ID = U.ATTENDANCE_ID)) " +
		    "    AND L.STATUS = 'Approved' " +
		    "), 0) AS leavesTaken " +
		    "FROM " +
		    "USER_MASTER_ATTENDANCE_MOD_TBL U " +
		    "JOIN " +
		    "EMPLOYEE_PROFILE_TBL E " +
		    "ON U.ATTENDANCE_ID = E.EMP_ID " +
		    "WHERE TO_CHAR(U.ATTENDANCE_DATE, 'MM-YYYY') = :month " +
		    "GROUP BY " +
		    "U.ATTENDANCE_ID, " +
		    "E.EMPLOYEE_NAME " +
		    "ORDER BY " +
		    "U.ATTENDANCE_ID ASC",
		    nativeQuery = true)
    List<Object[]> getAttendanceDetailsForMonth(String month);
 

	@Query(value =

	"SELECT " +

			"U.ATTENDANCE_DATE AS attendanceDate, " +

			"U.STATUS AS status, " +

			"U.CHECK_IN_TIME AS checkin, " +

			"U.CHECK_OUT_TIME AS checkout, " +

			"U.TOTAL_HOURS_WORKED AS totalHoursWorked, " +

			"U.ATTENDANCE_ID AS attendanceId " +

			"FROM USER_MASTER_ATTENDANCE_MOD_TBL U " +

			"WHERE U.ATTENDANCE_ID = :attendanceId " +

			"AND U.ATTENDANCE_DATE >= TO_DATE(:month, 'MM-YYYY') - INTERVAL '1' MONTH - (TO_CHAR(TO_DATE(:month, 'MM-YYYY'), 'DD') - 27) "
			+

			"AND U.ATTENDANCE_DATE < TO_DATE(:month, 'MM-YYYY') + 26 " +

			"ORDER BY U.ATTENDANCE_DATE ASC",

			nativeQuery = true)

	List<Object[]> getEmployeeDetailedReportForMonth(String attendanceId, String month);

	@Query(value =

	"SELECT " +

			"U.ATTENDANCE_ID AS attendanceId, " +

			"TO_CHAR(U.ATTENDANCE_DATE, 'MM-YYYY') AS monthYear, " +

			"COUNT(DISTINCT U.ATTENDANCE_DATE) AS totalDaysWorked, " +

			"LPAD(" +

			"TRUNC(SUM(" +

			"CASE " +

			"WHEN REGEXP_LIKE(U.TOTAL_HOURS_WORKED, '^\\d{1,2}:\\d{2}$') " +

			"THEN " +

			"(TO_NUMBER(SUBSTR(U.TOTAL_HOURS_WORKED, 1, INSTR(U.TOTAL_HOURS_WORKED, ':') - 1)) * 60 " +

			"+ TO_NUMBER(SUBSTR(U.TOTAL_HOURS_WORKED, INSTR(U.TOTAL_HOURS_WORKED, ':') + 1))) " +

			"ELSE 0 " +

			"END" +

			") / 60), 3, '0') || ':' || " +

			"LPAD(" +

			"MOD(SUM(" +

			"CASE " +

			"WHEN REGEXP_LIKE(U.TOTAL_HOURS_WORKED, '^\\d{1,2}:\\d{2}$') " +

			"THEN " +

			"(TO_NUMBER(SUBSTR(U.TOTAL_HOURS_WORKED, 1, INSTR(U.TOTAL_HOURS_WORKED, ':') - 1)) * 60 " +

			"+ TO_NUMBER(SUBSTR(U.TOTAL_HOURS_WORKED, INSTR(U.TOTAL_HOURS_WORKED, ':') + 1))) " +

			"ELSE 0 " +

			"END" +

			"), 60), 2, '0') AS totalHoursWorked, " +

			"(EXTRACT(DAY FROM LAST_DAY(MIN(U.ATTENDANCE_DATE))) - COUNT(DISTINCT U.ATTENDANCE_DATE)) AS remainingDays "
			+

			"FROM USER_MASTER_ATTENDANCE_MOD_TBL U " +

			"GROUP BY U.ATTENDANCE_ID, TO_CHAR(U.ATTENDANCE_DATE, 'MM-YYYY') " +

			"ORDER BY TO_DATE(TO_CHAR(U.ATTENDANCE_DATE, 'MM-YYYY'), 'MM-YYYY'), U.ATTENDANCE_ID",

			nativeQuery = true)

	List<Object[]> getEmployeeLeaveSummaryForAllEmployees();




}
