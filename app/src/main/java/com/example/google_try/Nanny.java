package com.example.google_try;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Nanny implements DBInterface{

    private String firstName;
    private String lastName;
    private String age;
    private String numberPhone;
    private String email;
    private boolean smoker;
    private boolean hasDrivingLicense;
    private String uid = "";
    private List<String> languages = new ArrayList<>();

    private String perHour;

    private boolean experienceWithChildren;

    private String contentAboutNanny;

    private String profilePicture = "";

    public double getLat() {
        return lat;
    }

    public Nanny setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Nanny setLon(double lon) {
        this.lon = lon;
        return this;
    }

    private double lat = 0.0;

    private double lon = 0.0;




    public String getProfilePicture() {
        return profilePicture;
    }

    public Nanny setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public String getContentAboutNanny() {
        return contentAboutNanny;
    }

    public Nanny setContentAboutNanny(String contentAboutNanny) {
        this.contentAboutNanny = contentAboutNanny;
        return this;
    }

    public boolean isExperienceWithChildren() {
        return experienceWithChildren;
    }

    public Nanny setExperienceWithChildren(boolean experienceWithChildren) {
        this.experienceWithChildren = experienceWithChildren;
        return this;
    }

    public String getPerHour() {
        return perHour;
    }

    public Nanny setPerHour(String perHour) {
        this.perHour = perHour;
        return this;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public Nanny setContent(String content) {
        this.content = content;
        return this;
    }

    public Nanny(){
    }

    public String getUid(){ return uid; }

    public Nanny setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Nanny setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Nanny setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getAge() {
        return age;
    }

    public Nanny setAge(String age) {
        this.age = age;
        return this;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public Nanny setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Nanny setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isSmoker() {
        return smoker;
    }

    public Nanny setSmoker(boolean smoker) {
        this.smoker = smoker;
        return this;
    }

    public boolean isHasDrivingLicense() {
        return hasDrivingLicense;
    }

    public Nanny setHasDrivingLicense(boolean hasDrivingLicense) {
        this.hasDrivingLicense = hasDrivingLicense;
        return this;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public Nanny setLanguages(List<String> languages) {
        this.languages = languages;
        return this;
    }

    @Override
    public String toString() {
        return "Nanny{" +
                "firstName='" + firstName + '\'' +
                ", laseName='" + lastName + '\'' +
                ", age='" + age + '\'' +
                ", numberPhone='" + numberPhone + '\'' +
                ", email='" + email + '\'' +
                ", isSmoker=" + smoker +
                ", hasDrivingLicense=" + hasDrivingLicense +
                ", uid='" + uid + '\'' +
                ", languages=" + languages +
                '}';
    }

    @Override
    public void loadToDataBade() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Nanny_User");
        String id = this.getUid();
        databaseReference.child(id).setValue(this).addOnFailureListener(e -> {

        });
    }
}
