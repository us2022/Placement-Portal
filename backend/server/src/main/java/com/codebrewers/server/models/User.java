package com.codebrewers.server.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.codebrewers.server.shared.UserName;
import com.codebrewers.server.shared.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_account")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_account_id")
    private Long userAccountId;

    @Embedded
    private UserName userName;

    @Column(name = "user_mobile_number", length = 10, nullable = false, unique = true)
    private String mobileNumber;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @CreatedDate
    private long createdDate;

    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @Column(name = "user_admin_approval_status")
    private Boolean adminApprovalStatus;

    @Column(name = "user_status_enabled")
    private Boolean enabled;

    @ManyToOne
    private Company userCompany;

    @ManyToOne
    private College userCollege;

    @JsonIgnore
    @OneToMany(mappedBy = "companyUser")
    private List<JobPost> companyUserJobPost;

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})'
    @ManyToMany
    private List<JobPost> collegeUserJobPost;

    public User() {
    }

    public User(UserName userName, String mobileNumber, String email, String password, UserType userType) {
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.adminApprovalStatus = false;
        this.enabled = true;
    }

    public User(UserName userName, String mobileNumber, String email, String password, long createdDate,
            UserType userType, Boolean adminApprovalStatus, Boolean enabled, Company userCompany, College userCollege,
            List<JobPost> companyUserJobPost, List<JobPost> collegeUserJobPost) {
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.userType = userType;
        this.adminApprovalStatus = adminApprovalStatus;
        this.enabled = enabled;
        this.userCompany = userCompany;
        this.userCollege = userCollege;
        this.companyUserJobPost = companyUserJobPost;
        this.collegeUserJobPost = collegeUserJobPost;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    public UserName getUserName() {
        return userName;
    }

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Company getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(Company userCompany) {
        this.userCompany = userCompany;
    }

    public College getUserCollege() {
        return userCollege;
    }

    public void setUserCollege(College userCollege) {
        this.userCollege = userCollege;
    }

    public Boolean getAdminApprovalStatus() {
        return adminApprovalStatus;
    }

    public void setAdminApprovalStatus(Boolean adminApprovalStatus) {
        this.adminApprovalStatus = adminApprovalStatus;
    }

    @JsonIgnore
    public List<JobPost> getCompanyUserJobPost() {
        return companyUserJobPost;
    }

    @JsonIgnore
    public void setCompanyUserJobPost(List<JobPost> companyUserJobPost) {
        this.companyUserJobPost = companyUserJobPost;
    }

    public List<JobPost> getCollegeUserJobPost() {
        return collegeUserJobPost;
    }

    public void setCollegeUserJobPost(List<JobPost> collegeUserJobPost) {
        this.collegeUserJobPost = collegeUserJobPost;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
