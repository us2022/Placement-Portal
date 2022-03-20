package com.codebrewers.server.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.codebrewers.server.shared.JobDomain;
import com.codebrewers.server.shared.JobOpenFor;
import com.codebrewers.server.shared.JobType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="job_post")
public class JobPost  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="job_post_id")
    private Long jobPostId;

    @Column(name="job_role", nullable=false)
    private String jobRole; // Eg. SDE, Data Scientist etc.

    @Column(name="job_description", nullable=false)
    private String jobDescription;

    @Column(name="job_type", nullable=false)
    private JobType jobType; // Full-time/Part-time etc.

    @Column(name="job_domain", nullable=false)
    private JobDomain jobDomain; // Technical/Marketing/Design etc.

    @Column(name="job_compensation", nullable=false)
    private double jobCompensation;

    @Column(name="job_open_for", nullable=false)
    private JobOpenFor jobOpenFor;


    @ManyToMany
    private List<Location> jobLocation;

    @ManyToMany
    private List<SkillSet> skillSet;

    @ManyToMany
    private List<Qualification> qualification;

    @ManyToOne
    private User companyUser;

    /*jobpost{
        jobRole,
        jobDescription,.....
        jobLocation,
        skillset,
        qualification,
        companyUser
//    } */

// @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIgnore
@ManyToMany(mappedBy = "collegeUserJobPost",fetch = FetchType.EAGER)
List<User> collegeUsers;

    public JobPost() {
    }

    public JobPost(String jobRole, String jobDescription, JobType jobType, JobDomain jobDomain, double jobCompensation, JobOpenFor jobOpenFor, List<Location> jobLocation, List<SkillSet> skillSet, List<Qualification> qualification, User companyUser, List<User> collegeUsers) {
        this.jobRole = jobRole;
        this.jobDescription = jobDescription;
        this.jobType = jobType;
        this.jobDomain = jobDomain;
        this.jobCompensation = jobCompensation;
        this.jobOpenFor = jobOpenFor;
        this.jobLocation = jobLocation;
        this.skillSet = skillSet;
        this.qualification = qualification;
        this.companyUser = companyUser;
        this.collegeUsers = collegeUsers;
    }

    public Long getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Long jobPostId) {
        this.jobPostId = jobPostId;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public JobDomain getJobDomain() {
        return jobDomain;
    }

    public void setJobDomain(JobDomain jobDomain) {
        this.jobDomain = jobDomain;
    }

    public double getJobCompensation() {
        return jobCompensation;
    }

    public void setJobCompensation(double jobCompensation) {
        this.jobCompensation = jobCompensation;
    }

    public JobOpenFor getJobOpenFor() {
        return jobOpenFor;
    }

    public void setJobOpenFor(JobOpenFor jobOpenFor) {
        this.jobOpenFor = jobOpenFor;
    }

    public List<Location> getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(List<Location> jobLocation) {
        this.jobLocation = jobLocation;
    }

    public List<SkillSet> getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(List<SkillSet> skillSet) {
        this.skillSet = skillSet;
    }

    public List<Qualification> getQualification() {
        return qualification;
    }

    public void setQualification(List<Qualification> qualification) {
        this.qualification = qualification;
    }

    public User getCompanyUser() {
        return companyUser;
    }

    public void setCompanyUser(User companyUser) {
        this.companyUser = companyUser;
    }

    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public List<User> getCollegeUsers() {
        return collegeUsers;
    }

    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public void setCollegeUsers(List<User> collegeUsers) {
        this.collegeUsers = collegeUsers;
    }
}
