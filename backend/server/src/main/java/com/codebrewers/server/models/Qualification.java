package com.codebrewers.server.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "qualification")
public class Qualification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "qualification_id")
    private Long qualificationId;

    @Column(name = "qualification_name", nullable = false)
    private String qualificationName; // btech

    @Column(name = "qualification_specialization")
    private String qualificationSpecialization; // ece

    @JsonIgnore
    @ManyToMany(mappedBy = "qualification")
    private List<JobPost> jobPost;

    public Qualification() {
    }

    public Qualification(String qualificationName, String qualificationSpecialization, List<JobPost> jobPost) {
        this.qualificationName = qualificationName;
        this.qualificationSpecialization = qualificationSpecialization;
        this.jobPost = jobPost;
    }

    public Long getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(Long qualificationId) {
        this.qualificationId = qualificationId;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public String getQualificationSpecialization() {
        return qualificationSpecialization;
    }

    public void setQualificationSpecialization(String qualificationSpecialization) {
        this.qualificationSpecialization = qualificationSpecialization;
    }

    @JsonIgnore
    public List<JobPost> getJobPost() {
        return jobPost;
    }

    @JsonIgnore
    public void setJobPost(List<JobPost> jobPost) {
        this.jobPost = jobPost;
    }
}
