package com.whitestone.hrms.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.TraineeMaster;

@EnableJpaRepositories(basePackages = "com.whitestone.hrms.repo")

@Repository
public interface TraineemasterRepository extends JpaRepository<TraineeMaster, String> {
	
	// 🔹 Find active trainee by trngid or userid
    @Query("SELECT u FROM TraineeMaster u WHERE (u.trngid = :Trngid OR u.userid = :Trngid) AND u.status = 'Active'")
    Optional<TraineeMaster> findByTrngidOrUserId(@Param("Trngid") String Trngid);

    // 🔹 Find all active trainees who report to a specific trainee
    @Query("SELECT u FROM TraineeMaster u WHERE u.repoteTo = :Trngid AND u.status = 'Active'")
    List<TraineeMaster> findByRepoteToCustom(@Param("Trngid") String Trngid);

    // 🔹 Same as above, but using simple parameter name
    @Query("SELECT u FROM TraineeMaster u WHERE u.repoteTo = :repoteTo AND u.status = 'Active'")
    List<TraineeMaster> findByRepoteTo(@Param("repoteTo") String repoteTo);

    // 🔹 Find single active trainee by trngid
    @Query("SELECT u FROM TraineeMaster u WHERE u.trngid = :Trngid AND u.status = 'Active'")
    TraineeMaster findByTrngid(@Param("Trngid") String Trngid);

    // 🔹 Find all active trainees by a list of trngids
    @Query("SELECT u FROM TraineeMaster u WHERE u.trngid IN :Trngids AND u.status = 'Active'")
    List<TraineeMaster> findByTrngidIn(@Param("Trngids") List<String> Trngids);

    // 🔹 Check if an active trainee exists
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM TraineeMaster u WHERE u.trngid = :trngid AND u.status = 'Active'")
    boolean existsByTrngid(@Param("trngid") String trngid);
}