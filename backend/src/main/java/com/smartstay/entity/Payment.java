package com.smartstay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="payments")
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank private String tenantName;
    @NotNull @DecimalMin("0.01") private BigDecimal amount;
    @NotNull private LocalDate paidOn = LocalDate.now();
    @NotBlank private String monthLabel;
    public Long getId(){return id;} public String getTenantName(){return tenantName;}
    public void setTenantName(String v){tenantName=v;} public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal v){amount=v;} public LocalDate getPaidOn(){return paidOn;}
    public void setPaidOn(LocalDate v){paidOn=v;} public String getMonthLabel(){return monthLabel;}
    public void setMonthLabel(String v){monthLabel=v;}
}
