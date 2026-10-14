package com.smartstay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="tenants")
public class Tenant {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank private String fullName;
    @NotBlank @Column(unique=true) private String phone;
    @Email private String email;
    private String roomNumber;
    public Long getId(){return id;} public String getFullName(){return fullName;}
    public void setFullName(String v){fullName=v;} public String getPhone(){return phone;}
    public void setPhone(String v){phone=v;} public String getEmail(){return email;}
    public void setEmail(String v){email=v;} public String getRoomNumber(){return roomNumber;}
    public void setRoomNumber(String v){roomNumber=v;}
}
