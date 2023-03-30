package com.renatofranco.bsalecheckin.services.flight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renatofranco.bsalecheckin.dtos.FlightDTO;
import com.renatofranco.bsalecheckin.dtos.PassengerDTO;
import com.renatofranco.bsalecheckin.entities.Airplane;
import com.renatofranco.bsalecheckin.entities.BoardingPass;
import com.renatofranco.bsalecheckin.entities.Flight;
import com.renatofranco.bsalecheckin.mappers.FlightMapper;
import com.renatofranco.bsalecheckin.mappers.PassengerMapper;
import com.renatofranco.bsalecheckin.repositories.IFlightRepository;
import com.renatofranco.bsalecheckin.util.seatallocator.AirplaneSeatDistribution;
import com.renatofranco.bsalecheckin.util.seatallocator.SeatAllocator;
import com.renatofranco.bsalecheckin.util.seatallocator.TravelClass;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FlightService implements IFlightService {

  @Autowired
  private IFlightRepository flightRepository;

  @Autowired
  private FlightMapper flightMapper;

  @Autowired
  private PassengerMapper passengerMapper;

  @Override
  public FlightDTO findFlightById(long id) {
    Flight flight = this.flightRepository.findById(id).orElseThrow(EntityNotFoundException::new);

    FlightDTO flightDTO = flightMapper.toDto(flight, flight.getAirplane());

    List<PassengerDTO> passengers = passengerMapper.toDtoList(this.assignRemainingSeats(flight.getBoardingPasses(),
        flight.getAirplane()));

    flightDTO.setPassengers(passengers);
    return flightDTO;
  }

  private List<BoardingPass> assignRemainingSeats(List<BoardingPass> boardingPasses, Airplane airplane) {

    AirplaneSeatDistribution seatDistribution = AirplaneSeatDistribution.builder()
        .firstClass(new TravelClass(airplane.getFirstClassRowNames(), airplane.getFirstClassColumnDistribution(),
            airplane.getFirstClassRowMinMax()))
        .premiumEconomyClass(new TravelClass(airplane.getPremiumEconomyClassRowNames(),
            airplane.getPremiumEconomyClassColumnDistribution(), airplane.getPremiumEconomyClassRowMinMax()))
        .economyClass(new TravelClass(airplane.getEconomyClassRowNames(), airplane.getEconomyClassColumnDistribution(),
            airplane.getEconomyClassRowMinMax()))
        .build();

    List<BoardingPass> boardingPassesWithSeat = boardingPasses.stream()
        .filter(boardingPass -> !Objects.isNull(boardingPass.getSeat())).collect(Collectors.toList());

    SeatAllocator seatAllocator = new SeatAllocator(seatDistribution, boardingPassesWithSeat);

    Map<Long, List<BoardingPass>> boardingPassesPerPurchase = boardingPasses.stream()
        .collect(Collectors.groupingBy(boardingPass -> boardingPass.getPurchase().getId(), Collectors.toList()));

    List<List<BoardingPass>> groupedBoardingPasses = new ArrayList<>(boardingPassesPerPurchase.values());

    List<List<BoardingPass>> groupsWithAnySeatAssigned = groupedBoardingPasses.stream()
        .filter(group -> group.stream().anyMatch(boardingPass -> !Objects.isNull(boardingPass.getSeat())))
        .collect(Collectors.toList());

    List<List<BoardingPass>> groupsWithNoSeatAssignedWithChildren = groupedBoardingPasses.stream()
        .filter(group -> group.stream().allMatch(boardingPass -> Objects.isNull(boardingPass.getSeat()))
            && group.stream().anyMatch(boardingPass -> boardingPass.getPassenger().isUnderAge()))
        .collect(Collectors.toList());

    List<List<BoardingPass>> groupsWithNoSeatAssignedWithNoChildren = groupedBoardingPasses.stream()
        .filter(group -> group.stream().allMatch(boardingPass -> Objects.isNull(boardingPass.getSeat()))
            && group.stream().allMatch(boardingPass -> !boardingPass.getPassenger().isUnderAge()))
        .collect(Collectors.toList());

    Collections.sort(groupsWithNoSeatAssignedWithNoChildren,
        Comparator.comparing(List::size, Comparator.reverseOrder()));

    boardingPasses = groupsWithAnySeatAssigned.stream().map(groupBoardingPasses -> {
      return seatAllocator.allocate(groupBoardingPasses);
    }).flatMap(Collection::stream).collect(Collectors.toList());

    boardingPasses.addAll(groupsWithNoSeatAssignedWithChildren.stream().map(groupBoardingPasses -> {
      return seatAllocator.allocate(groupBoardingPasses);
    }).flatMap(Collection::stream).collect(Collectors.toList()));

    boardingPasses.addAll(groupsWithNoSeatAssignedWithNoChildren.stream().map(groupBoardingPasses -> {
      return seatAllocator.allocate(groupBoardingPasses);
    }).flatMap(Collection::stream).collect(Collectors.toList()));

    return boardingPasses;
  }

  @Override
  public List<Flight> getFlights() {
    return flightRepository.findAll();
  }
}
