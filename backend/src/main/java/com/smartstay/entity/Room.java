package com.smartstay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name="rooms")
public class Room {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank private String roomNumber;
    @Min(1) private int beds;
    @Min(0) private int occupiedBeds;
    @DecimalMin("0.0") private BigDecimal monthlyRent = BigDecimal.ZERO;
    public Long getId(){return id;} public String getRoomNumber(){return roomNumber;}
    public void setRoomNumber(String v){roomNumber=v;} public int getBeds(){return beds;}
    public void setBeds(int v){beds=v;} public int getOccupiedBeds(){return occupiedBeds;}
    public void setOccupiedBeds(int v){occupiedBeds=v;} public BigDecimal getMonthlyRent(){return monthlyRent;}
    public void setMonthlyRent(BigDecimal v){monthlyRent=v;}
}
