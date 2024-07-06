package com.example.pets.dto.response.clientFillerResponse;

import com.example.pets.dto.response.clientFillerResponse.location.LocationResponse;
import lombok.Data;

@Data
public class ClientFillerResponseMain {
  private NameResponse name;
  private LocationResponse location;
  private String email;
  private String cell;
  private LoginResponse login; //aka pet name
}
