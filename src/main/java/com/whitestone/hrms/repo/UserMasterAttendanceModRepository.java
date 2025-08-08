package com.whitestone.hrms.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.UserMasterAttendance;
import com.whitestone.entity.UserMasterAttendanceMod;

@Repository
public interface UserMasterAttendanceModRepository extends JpaRepository<UserMasterAttendanceMod, String> {
	
	@Query("SELECT u FROM UserMasterAttendanceMod u WHERE u.attendanceid = :attendanceid AND TRUNC(u.attendancedate) = TRUNC(:attendancedate)")
	Optional<UserMasterAttendanceMod> findByAttendanceidAndAttendancedate(@Param("attendanceid") String attendanceid,
			@Param("attendancedate") Date date);

	@Query("SELECT a FROM UserMasterAttendanceMod a WHERE a.srlnum = (SELECT MAX(b.srlnum) FROM UserMasterAttendanceMod b WHERE b.attendanceid = :attendanceId AND TRUNC(b.attendancedate) = TRUNC(:attendancedate))")
	Optional<UserMasterAttendanceMod> findLatestRecordBySrlNo(@Param("attendanceId") String attendanceId,@Param("attendancedate") Date date);
	
	@Query("SELECT u FROM UserMasterAttendanceMod u WHERE u.attendanceid = :attendanceid AND u.attendancedate BETWEEN :startDate AND :endDate")
	List<UserMasterAttendanceMod> findAttendanceByEmployeeIdAndDateRange(@Param("attendanceid") String employeeId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);
		
	@Query("SELECT u FROM UserMasterAttendanceMod u WHERE u.attendancedate BETWEEN :startDate AND :endDate")
	List<UserMasterAttendanceMod> findAttendanceByDateRange(@Param("startDate") Date startDate, 
	                                                         @Param("endDate") Date endDate);

	@Query("SELECT a FROM UserMasterAttendanceMod a " +
		       "WHERE a.attendanceid = :employeeId " +
		       "AND a.attendancedate BETWEEN :startDate AND :endDate " +
		       "AND a.checkouttime IS NULL " +
		       "ORDER BY a.srlnum DESC")
		Optional<UserMasterAttendanceMod> findLatestUncheckedOutRecordBetweenDates(
		    @Param("employeeId") String employeeId,
		    @Param("startDate") Date startDate,
		    @Param("endDate") Date endDate);
		
	@Query("SELECT u FROM UserMasterAttendanceMod u WHERE u.attendanceid = :attendanceId AND u.attendancedate BETWEEN :startDate AND :endDate")
	List<UserMasterAttendanceMod> findByAttendanceidAndDateRange(
	    @Param("attendanceId") String attendanceId,
	    @Param("startDate") Date startDate,
	    @Param("endDate") Date endDate
	);
	
	@Query("SELECT u FROM UserMasterAttendanceMod u WHERE u.attendancedate >= :start AND u.attendancedate < :end AND u.attendanceid IN :attendanceIds")
	List<UserMasterAttendanceMod> findAllByAttendancedateAndAttendanceidIn(
	    @Param("start") Date start,
	    @Param("end") Date end,
	    @Param("attendanceIds") List<String> attendanceIds
	);


	@Query("SELECT u FROM UserMasterAttendanceMod u WHERE u.attendanceid = :attendanceid AND TRUNC(u.srlnum) = TRUNC(:srlnum)")
	Optional<UserMasterAttendanceMod> findByAttendanceidAndSrlnum(@Param("attendanceid") String attendanceid,
			@Param("srlnum") Long srlnum);
}