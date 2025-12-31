package com.whitestone.hrms.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.WsslCalendarMod;

@Repository
public interface WsslCalendarModRepository extends JpaRepository<WsslCalendarMod, Long> {
    
    @Query("SELECT w FROM WsslCalendarMod w WHERE w.eventDate >= :currentDate ORDER BY w.eventDate ASC")
    List<WsslCalendarMod> findByEventDateAfter(@Param("currentDate") java.sql.Date currentDate);

    List<WsslCalendarMod> findByEventDateBetween(Date startDate, Date endDate);
    
    // Check if event already exists for the same date - SIMPLIFIED version
    @Query("SELECT w FROM WsslCalendarMod w WHERE w.eventDate = :eventDate")
    List<WsslCalendarMod> findByEventDate(@Param("eventDate") Date eventDate);
}