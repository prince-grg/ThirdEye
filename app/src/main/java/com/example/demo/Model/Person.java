package com.example.demo.Model;

public class Person {
    private String name,age,gender,address,number,fathername,imageLocation;

    public Person(String name, String age, String gender, String address, String number, String fathername, String imageLocation) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.number = number;
        this.fathername = fathername;
        this.imageLocation = imageLocation;
    }

    public Person(String name, String age, String gender, String imageLocation) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.imageLocation = imageLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
}
