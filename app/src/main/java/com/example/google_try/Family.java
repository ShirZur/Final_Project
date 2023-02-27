package com.example.google_try;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Family implements DBInterface {

    private String numberPhone, family_name;

    private String email, uid, profilePicture, number_of_kids, hourly_wage, content_of_family;

    private boolean baby;
    private boolean pets;
    private boolean toddle, preschooler, student, adolescent,  cooking
            ,hw, houseChores;

    private double lat, lon;

    public double getLat() {
        return lat;
    }

    public Family setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Family setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public Family() {
    }

    public String getFamily_name() {
        return family_name;
    }

    public Family setFamily_name(String family_name) {
        this.family_name = family_name;
        return this;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public Family setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Family setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public Family setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public Family setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public String getNumber_of_kids() {
        return number_of_kids;
    }

    public Family setNumber_of_kids(String number_of_kids) {
        this.number_of_kids = number_of_kids;
        return this;
    }

    public String getHourly_wage() {
        return hourly_wage;
    }

    public Family setHourly_wage(String hourly_wage) {
        this.hourly_wage = hourly_wage;
        return this;
    }

    public String getContent_of_family() {
        return content_of_family;
    }

    public Family setContent_of_family(String content_of_family) {
        this.content_of_family = content_of_family;
        return this;
    }

    public boolean isBaby() {
        return baby;
    }

    public Family setBaby(boolean baby) {
        this.baby = baby;
        return this;
    }



    public boolean isPets() {
        return pets;
    }

    public Family setPets(boolean pets) {
        this.pets = pets;
        return this;
    }

    public boolean isToddle() {
        return toddle;
    }

    public Family setToddle(boolean toddle) {
        this.toddle = toddle;
        return this;
    }

    public boolean isPreschooler() {
        return preschooler;
    }

    public Family setPreschooler(boolean preschooler) {
        this.preschooler = preschooler;
        return this;
    }

    public boolean isStudent() {
        return student;
    }

    public Family setStudent(boolean student) {
        this.student = student;
        return this;
    }

    public boolean isAdolescent() {
        return adolescent;
    }

    public Family setAdolescent(boolean adolescent) {
        this.adolescent = adolescent;
        return this;
    }


    public boolean isCooking() {
        return cooking;
    }

    public Family setCooking(boolean cooking) {
        this.cooking = cooking;
        return this;
    }

    public boolean isHw() {
        return hw;
    }

    public Family setHw(boolean hw) {
        this.hw = hw;
        return this;
    }

    public boolean isHouseChores() {
        return houseChores;
    }

    public Family setHouseChores(boolean houseChores) {
        this.houseChores = houseChores;
        return this;
    }

    @Override
    public void loadToDataBade() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Family_User");
        String id = this.getUid();
        databaseReference.child(id).setValue(this).addOnFailureListener(e -> {

        });
    }
}
