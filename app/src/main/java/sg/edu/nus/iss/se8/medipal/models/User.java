package sg.edu.nus.iss.se8.medipal.models;

import java.util.Date;

public class User {
    private Integer id;
    private String name;
    private Date birthDate;
    private String identityNumber;
    private String address;
    private String postalCode;
    private Double height;
    private String bloodType;

    public User() {
    }

    public User(Integer id, String name, Date birthDate, String identityNumber, String address, String postalCode, Double height, String bloodType) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.identityNumber = identityNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.height = height;
        this.bloodType = bloodType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}
