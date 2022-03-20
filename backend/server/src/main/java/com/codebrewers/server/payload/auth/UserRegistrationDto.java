package com.codebrewers.server.payload.auth;

import com.codebrewers.server.shared.UserName;

public class UserRegistrationDto {
    private UserName userName;
    private String mobileNumber;
    private String email;
    private String password;

    UserRegistrationDto() {

    }

    public UserRegistrationDto(UserName userName, String mobileNumber, String email, String password) {
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;

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

}
