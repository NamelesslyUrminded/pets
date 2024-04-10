package com.example.pets.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class OwnerResponse extends Response implements Serializable {

    private Long id;

    private String name;

    private String secondName;

    private String phoneNumber;

    private String address;

    private String emailAddress;
}
