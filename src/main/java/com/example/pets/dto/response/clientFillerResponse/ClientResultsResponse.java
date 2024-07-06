package com.example.pets.dto.response.clientFillerResponse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClientResultsResponse {

  private List<ClientFillerResponseMain> results = new ArrayList<>();
}
