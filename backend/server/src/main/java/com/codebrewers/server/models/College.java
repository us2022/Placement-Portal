package com.codebrewers.server.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "college")
public class College  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "college_id")
    private Long collegeId;

    @Column(name = "college_name", nullable = false)
    private String collegeName;

    @Column(name = "college_nirf_ranking", nullable = false)
    private int collegeNirfRanking;

    @Column(name = "college_aicte_affiliation", nullable = false)
    private boolean collegeAicteAffiliation;

    @Column(name = "college_website", nullable = false)
    private String collegeWebsite;

    @Column(name = "college_email", nullable = false)
    private String collegeEmail;

    @ManyToOne
    private Location collegeLocation;

    @JsonIgnore
    @OneToMany(mappedBy = "userCollege")
    private List<User> users;

    public College() {
    }

    public College(String collegeName, int collegeNirfRanking, boolean collegeAicteAffiliation, String collegeWebsite,
            String collegeEmail, Location collegeLocation, List<User> users) {
        this.collegeName = collegeName;
        this.collegeNirfRanking = collegeNirfRanking;
        this.collegeAicteAffiliation = collegeAicteAffiliation;
        this.collegeWebsite = collegeWebsite;
        this.collegeEmail = collegeEmail;
        this.collegeLocation = collegeLocation;
        this.users = users;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public int getCollegeNirfRanking() {
        return collegeNirfRanking;
    }

    public void setCollegeNirfRanking(int collegeNirfRanking) {
        this.collegeNirfRanking = collegeNirfRanking;
    }

    public boolean isCollegeAicteAffiliation() {
        return collegeAicteAffiliation;
    }

    public void setCollegeAicteAffiliation(boolean collegeAicteAffiliation) {
        this.collegeAicteAffiliation = collegeAicteAffiliation;
    }

    // The @JsonManagedReference is used on the OneToMany side while the
    // @JsonBackReference is used at the @ManyToOne side.
    public Location getCollegeLocation() {
        return collegeLocation;
    }

    public void setCollegeLocation(Location collegeLocation) {
        this.collegeLocation = collegeLocation;
    }

    public String getCollegeWebsite() {
        return collegeWebsite;
    }

    public void setCollegeWebsite(String collegeWebsite) {
        this.collegeWebsite = collegeWebsite;
    }

    public String getCollegeEmail() {
        return collegeEmail;
    }

    public void setCollegeEmail(String collegeEmail) {
        this.collegeEmail = collegeEmail;
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
