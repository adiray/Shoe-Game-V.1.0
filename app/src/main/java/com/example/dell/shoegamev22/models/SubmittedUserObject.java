package com.example.dell.shoegamev22.models;

public class SubmittedUserObject {


    private String firstName, lastName, eMail, phone, password;
    private Boolean stayLoggedIn, agreeToTermsAndConditions;
    private Integer gender;  //none selected:1, male:2, female:3

    public SubmittedUserObject(String eMail, String password, Boolean stayLoggedIn) {
        this.eMail = eMail;
        this.password = password;
        this.stayLoggedIn = stayLoggedIn;
    }

    public SubmittedUserObject(String firstName, String lastName, String eMail, String phone, String password, Boolean stayLoggedIn, Boolean agreeToTermsAndConditions, Integer gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.phone = phone;
        this.password = password;
        this.stayLoggedIn = stayLoggedIn;
        this.agreeToTermsAndConditions = agreeToTermsAndConditions;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getStayLoggedIn() {
        return stayLoggedIn;
    }

    public void setStayLoggedIn(Boolean stayLoggedIn) {
        this.stayLoggedIn = stayLoggedIn;
    }

    public Boolean getAgreeToTermsAndConditions() {
        return agreeToTermsAndConditions;
    }

    public void setAgreeToTermsAndConditions(Boolean agreeToTermsAndConditions) {
        this.agreeToTermsAndConditions = agreeToTermsAndConditions;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
