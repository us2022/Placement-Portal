package com.codebrewers.server.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "company")
public class Company  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_description", nullable = false)
    private String companyDescription;

    @Column(name = "company_website", nullable = false)
    private String companyWebsite;

    @Column(name = "company_image", nullable = false)
    private String companyImage;

    @Column(name = "company_cin", nullable = false)
    private String companyCin;

    @JsonIgnore
    @OneToMany(mappedBy = "userCompany")
    private List<User> users;

    public Company() {
    }

    public Company(String companyName, String companyDescription, String companyWebsite, String companyImage,
            String companyCin, List<User> users) {
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.companyWebsite = companyWebsite;
        this.companyImage = companyImage;
        this.companyCin = companyCin;
        this.users = users;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public String getCompanyCin() {
        return companyCin;
    }

    public void setCompanyCin(String companyCin) {
        this.companyCin = companyCin;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    @JsonIgnore
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
