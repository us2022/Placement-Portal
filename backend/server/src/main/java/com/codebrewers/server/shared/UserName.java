package com.codebrewers.server.shared;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserName implements Serializable {

    @Column(name = "user_first_name", nullable = false)
    private String firstName;

    @Column(name = "user_middle_name", nullable = true)
    private String middleName;

    @Column(name = "user_last_name", nullable = false)
    private String lastName;

    public UserName() {
    }

    public UserName(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
