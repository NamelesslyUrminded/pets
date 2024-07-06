package com.example.pets.dto.response.clientFillerResponse.location;

import lombok.Data;


@Data
public class LocationResponse {
  private String country;
  private String city;
  private StreetResponse street;
}
