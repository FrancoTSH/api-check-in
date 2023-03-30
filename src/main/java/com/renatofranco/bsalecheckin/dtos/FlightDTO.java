package com.renatofranco.bsalecheckin.dtos;

import java.util.List;

import lombok.Data;

@Data
public class FlightDTO {
  private Integer flightId;
  private Long takeOffDateTime;
  private String takeOffAirport;
  private Long landingDateTime;
  private String landingAirport;
  private Long airplaneId;
  private List<PassengerDTO> passengers;
}
