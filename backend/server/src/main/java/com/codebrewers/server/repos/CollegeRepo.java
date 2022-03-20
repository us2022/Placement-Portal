package com.codebrewers.server.repos;

import java.util.List;

import com.codebrewers.server.models.College;
import com.codebrewers.server.models.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeRepo extends JpaRepository<College, Long> {
    
    List<College> findByCollegeName(String Name);

    List<College> findByCollegeLocation(Location location);

    List<College> findByCollegeNirfRanking(int nirf);

    List<College> findByOrderByCollegeNirfRankingAsc();

    List<College> findByCollegeAicteAffiliation(boolean flag);

    @Query(value = "SELECT * FROM college c WHERE (:name is null OR c.college_name=:name) AND (:nirf is null OR c.college_nirf_ranking=:nirf) AND (:aicte is null or c.college_aicte_affiliation=:aicte)", nativeQuery = true)
    List<College> findByCollegeCustom(@Param("name") String name, @Param("nirf") Integer nirfRanking,
            @Param("aicte") Boolean aicteAffiliation);
}
