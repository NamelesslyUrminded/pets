package com.example.pets.dto.request;

import lombok.Data;

@Data
public class OwnerRequest extends Request {

    private Long id;
    private String name;

    private String secondName;

    private String phoneNumber;

    private String address;

    private String emailAddress;
}
