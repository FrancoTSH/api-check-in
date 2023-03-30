package com.renatofranco.bsalecheckin.services.flight;

import java.util.List;

import com.renatofranco.bsalecheckin.dtos.FlightDTO;
import com.renatofranco.bsalecheckin.entities.Flight;

public interface IFlightService {
  public FlightDTO findFlightById(long id);

  public List<Flight> getFlights();
}
