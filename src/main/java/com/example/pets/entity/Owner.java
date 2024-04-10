package com.example.pets.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table
public class Owner extends BaseEntity {

    @Column
    public String name;

    @Column
    public String secondName;

    @Column
    public String phoneNumber;

    @Column
    public String address;

    @Column
    public String emailAddress;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clientList;
}
