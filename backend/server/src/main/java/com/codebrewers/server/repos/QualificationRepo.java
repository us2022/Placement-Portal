package com.codebrewers.server.repos;

import java.util.List;

import com.codebrewers.server.models.Qualification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationRepo extends JpaRepository<Qualification, Long> {

        @Query(value = "SELECT * FROM qualification q WHERE " +
                        "(:name is null OR q.qualification_name = :name) AND " +
                        "(:spec is null OR q.qualification_specialization = :spec)", nativeQuery = true)
        List<Qualification> findByQualificationCustom(@Param("name") String name,
                        @Param("spec") String spec);
}