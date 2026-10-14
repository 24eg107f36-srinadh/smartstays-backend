package com.smartstay.controller;

import com.smartstay.entity.*;
import com.smartstay.repository.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CrudController {
    private final RoomRepository rooms;
    private final TenantRepository tenants;
    private final PaymentRepository payments;
    public CrudController(RoomRepository rooms, TenantRepository tenants, PaymentRepository payments) {
        this.rooms=rooms; this.tenants=tenants; this.payments=payments;
    }

    @GetMapping("/health") public Map<String,String> health(){return Map.of("status","ok");}

    @GetMapping("/rooms") public List<Room> rooms(){return rooms.findAll();}
    @PostMapping("/rooms") public Room addRoom(@Valid @RequestBody Room room){return rooms.save(room);}
    @PutMapping("/rooms/{id}") public Room updateRoom(@PathVariable Long id,@Valid @RequestBody Room room){
        Room old=rooms.findById(id).orElseThrow(); old.setRoomNumber(room.getRoomNumber());
        old.setBeds(room.getBeds()); old.setOccupiedBeds(room.getOccupiedBeds());
        old.setMonthlyRent(room.getMonthlyRent()); return rooms.save(old);
    }
    @DeleteMapping("/rooms/{id}") public void deleteRoom(@PathVariable Long id){rooms.deleteById(id);}

    @GetMapping("/tenants") public List<Tenant> tenants(){return tenants.findAll();}
    @PostMapping("/tenants") public Tenant addTenant(@Valid @RequestBody Tenant tenant){return tenants.save(tenant);}
    @PutMapping("/tenants/{id}") public Tenant updateTenant(@PathVariable Long id,@Valid @RequestBody Tenant t){
        Tenant old=tenants.findById(id).orElseThrow(); old.setFullName(t.getFullName());
        old.setPhone(t.getPhone()); old.setEmail(t.getEmail()); old.setRoomNumber(t.getRoomNumber());
        return tenants.save(old);
    }
    @DeleteMapping("/tenants/{id}") public void deleteTenant(@PathVariable Long id){tenants.deleteById(id);}

    @GetMapping("/payments") public List<Payment> payments(){return payments.findAll();}
    @PostMapping("/payments") public Payment addPayment(@Valid @RequestBody Payment p){return payments.save(p);}
    @DeleteMapping("/payments/{id}") public void deletePayment(@PathVariable Long id){payments.deleteById(id);}

    @GetMapping("/dashboard") public Map<String,Object> dashboard(){
        var roomList=rooms.findAll(); var tenantList=tenants.findAll(); var paymentList=payments.findAll();
        int beds=roomList.stream().mapToInt(Room::getBeds).sum();
        int occupied=roomList.stream().mapToInt(Room::getOccupiedBeds).sum();
        double rent=paymentList.stream().mapToDouble(p->p.getAmount().doubleValue()).sum();
        return Map.of("rooms",roomList.size(),"tenants",tenantList.size(),
            "beds",beds,"occupiedBeds",occupied,"availableBeds",Math.max(0,beds-occupied),
            "paymentsRecorded",paymentList.size(),"rentCollected",rent);
    }
}
