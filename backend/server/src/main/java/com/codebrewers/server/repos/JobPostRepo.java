package com.codebrewers.server.repos;

import java.util.HashSet;
import java.util.List;

import com.codebrewers.server.models.JobPost;
import com.codebrewers.server.models.Location;
import com.codebrewers.server.models.Qualification;
import com.codebrewers.server.models.SkillSet;
import com.codebrewers.server.shared.JobDomain;
import com.codebrewers.server.shared.JobOpenFor;
import com.codebrewers.server.shared.JobType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostRepo extends JpaRepository<JobPost, Long> {
        
        List<JobPost> findByJobType(JobType jobType);

        List<JobPost> findByJobDomain(JobDomain jobDomain);

        List<JobPost> findByJobCompensation(double compensation);

        List<JobPost> findByJobRole(String jobRole);

        List<JobPost> findByJobOpenFor(JobOpenFor jobOpenFor);

        HashSet<JobPost> findByQualificationIn(List<Qualification> qualifications);

        HashSet<JobPost> findByJobLocationIn(List<Location> locations);

        HashSet<JobPost> findBySkillSetIn(List<SkillSet> skillSets);

        @Query(value = "SELECT * FROM job_post j WHERE " +
                        "(:jobrole is null OR j.job_role = :jobrole) AND " +
                        "(:jobtype is null OR j.job_type = :jobtype) AND " +
                        "(:jobdomain is null OR j.job_domain = :jobdomain) AND " +
                        "(:jobopenfor is null OR j.job_open_for = :jobopenfor) AND " +
                        "((:cmax is null AND :cmin is null) OR (j.job_compensation BETWEEN :cmax AND :cmin))", nativeQuery = true)
        List<JobPost> findByJobPostCustom(@Param("jobrole") String jobRole,
                        @Param("jobtype") JobType jobType,
                        @Param("jobdomain") JobDomain jobDomain,
                        @Param("jobopenfor") JobOpenFor jobOpenFor,
                        @Param("cmax") Double compensation_max,
                        @Param("cmin") Double compensation_min);
}
