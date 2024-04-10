package com.example.pets.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
@Entity
@Table
public class Pet extends BaseEntity {

    @Column
    public String name;

    @Column
    public LocalDate birthday;
}
