package com.example.demo.model;

import org.springframework.core.SpringVersion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user")
@CompoundIndex(def = "{'email' : 1},{'mobileNumber' : 1}", unique = true)
public class Form {
    @Id
    private String _id;
    private String name;
    private String email;
    private String mobileNumber;
    private List<String> hobbies;
    private String resumeId;
    private String password;


    public void set_Id(String profileId) {
        this._id = profileId;
    }

    public String get_Id() {
        return this._id;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setResumeId(String resume) {
        this.resumeId = resume;
    }

    public String getResumeId() {
        return this.resumeId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
