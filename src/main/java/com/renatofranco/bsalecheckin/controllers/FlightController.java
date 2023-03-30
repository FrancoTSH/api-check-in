package com.renatofranco.bsalecheckin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renatofranco.bsalecheckin.dtos.BaseResponse;
import com.renatofranco.bsalecheckin.dtos.FlightDTO;
import com.renatofranco.bsalecheckin.services.flight.FlightService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("flights")
@Tag(name = "Flights", description = "Informacion de los vuelos")
public class FlightController {

  @Autowired
  private FlightService flightService;

  @GetMapping("/{id}/passengers")
  @Operation(summary = "Obtiene los datos del vuelo junto con sus pasajeros")
  @Parameter(name = "id", description = "ID del vuelo")
  public ResponseEntity<BaseResponse<FlightDTO>> getFlightWithPassengers(@PathVariable("id") Long id) {
    FlightDTO flight = this.flightService.findFlightById(id);

    return ResponseEntity.ok(BaseResponse.<FlightDTO>builder().data(flight).build());
  }
}
