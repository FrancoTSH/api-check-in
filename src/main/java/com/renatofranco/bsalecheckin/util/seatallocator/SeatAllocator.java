package com.renatofranco.bsalecheckin.util.seatallocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import com.renatofranco.bsalecheckin.entities.Airplane;
import com.renatofranco.bsalecheckin.entities.BoardingPass;
import com.renatofranco.bsalecheckin.entities.Seat;

public class SeatAllocator {

  private List<List<AirplaneSeat>> firstClassSeats;
  private List<List<AirplaneSeat>> premiumEconomyClassSeats;
  private List<List<AirplaneSeat>> economyClassSeats;

  private TravelClass firstClassInfo;
  private TravelClass premiumEconomyInfo;
  private TravelClass economyInfo;

  public SeatAllocator(AirplaneSeatDistribution seatDistribution) {

    firstClassInfo = seatDistribution.getFirstClass();
    premiumEconomyInfo = seatDistribution.getPremiumEconomyClass();
    economyInfo = seatDistribution.getEconomyClass();

    firstClassSeats = this.generateSeatMap(firstClassInfo.getColumnDistribution(), firstClassInfo.getNumberOfRows());
    premiumEconomyClassSeats = this.generateSeatMap(premiumEconomyInfo.getColumnDistribution(),
        premiumEconomyInfo.getNumberOfRows());
    economyClassSeats = this.generateSeatMap(economyInfo.getColumnDistribution(), economyInfo.getNumberOfRows());
  }

  public SeatAllocator(AirplaneSeatDistribution seatDistribution, List<BoardingPass> boardingPasses) {

    this(seatDistribution);

    List<BoardingPass> firstClassBoardingPasses = boardingPasses.stream()
        .filter(boardingPass -> boardingPass.getSeatType().getName().equals(SeatClass.FIRST_CLASS.getLabel()))
        .collect(Collectors.toList());
    List<BoardingPass> premiumEconomyBoardingPasses = boardingPasses.stream()
        .filter(boardingPass -> boardingPass.getSeatType().getName().equals(SeatClass.PREMIUM_ECONOMY_CLASS.getLabel()))
        .collect(Collectors.toList());
    List<BoardingPass> economyClassBoardingPasses = boardingPasses.stream()
        .filter(boardingPass -> boardingPass.getSeatType().getName().equals(SeatClass.ECONOMY_CLASS.getLabel()))
        .collect(Collectors.toList());

    firstClassSeats = this.preAllocateSeats(firstClassSeats, firstClassBoardingPasses, firstClassInfo.getRowNames(),
        firstClassInfo.getRowMinMax());
    premiumEconomyClassSeats = this.preAllocateSeats(premiumEconomyClassSeats, premiumEconomyBoardingPasses,
        premiumEconomyInfo.getRowNames(), premiumEconomyInfo.getRowMinMax());
    economyClassSeats = this.preAllocateSeats(economyClassSeats, economyClassBoardingPasses, economyInfo.getRowNames(),
        economyInfo.getRowMinMax());
  }

  private List<List<AirplaneSeat>> generateSeatMap(List<Integer> columnDistribution, Integer numberOfRows) {
    List<List<AirplaneSeat>> seatMap = new ArrayList<>();

    for (int k = 0; k < numberOfRows; k++) {
      List<AirplaneSeat> seatRow = new ArrayList<>();
      Integer columnIndex = 0;
      for (int i = 0; i < columnDistribution.size(); i++) {

        Integer numberOfColums = columnDistribution.get(i);
        for (int j = 0; j < numberOfColums; j++) {
          if ((j == 0 && i == 0) || (j == numberOfColums - 1 && i == columnDistribution.size() - 1)) {
            seatRow.add(AirplaneSeat.builder().type(SeatType.WINDOW).rowIndex(k).columnIndex(columnIndex).build());
          } else if ((j == 0 && i > 0) || j == numberOfColums - 1) {
            seatRow.add(AirplaneSeat.builder().type(SeatType.AISLE).rowIndex(k).columnIndex(columnIndex).build());
          } else if (j > 0 && j <= numberOfColums - 1) {
            seatRow.add(AirplaneSeat.builder().type(SeatType.MIDDLE).rowIndex(k).columnIndex(columnIndex).build());
          }
          columnIndex = columnIndex + 1;
        }
      }

      seatMap.add(seatRow);
    }

    return seatMap;
  }

  private List<List<AirplaneSeat>> preAllocateSeats(List<List<AirplaneSeat>> seatMap, List<BoardingPass> boardingPasses,
      List<String> rowNames, List<Integer> rowMinMax) {

    boardingPasses.forEach(boardingPass -> {
      String column = boardingPass.getSeat().getColumn();
      Integer row = boardingPass.getSeat().getRow();
      Integer minRow = rowMinMax.get(0);
      AirplaneSeat seat = seatMap.get(row - minRow).get(rowNames.indexOf(column));
      seat.setAvailable(false);
      seat.setPurchaseId(boardingPass.getPurchase().getId());
      seat.setUnderAge(boardingPass.getPassenger().isUnderAge());
    });

    return seatMap;
  }

  public List<BoardingPass> allocate(List<BoardingPass> boardingPassesGroup) {

    Map<String, List<List<AirplaneSeat>>> seatClasses = new HashMap<String, List<List<AirplaneSeat>>>() {
      {
        put(SeatClass.FIRST_CLASS.getLabel(), firstClassSeats);
        put(SeatClass.PREMIUM_ECONOMY_CLASS.getLabel(), premiumEconomyClassSeats);
        put(SeatClass.ECONOMY_CLASS.getLabel(), economyClassSeats);
      }
    };

    Map<String, TravelClass> rowClassesInfo = new HashMap<String, TravelClass>() {
      {
        put(SeatClass.FIRST_CLASS.getLabel(), firstClassInfo);
        put(SeatClass.PREMIUM_ECONOMY_CLASS.getLabel(), premiumEconomyInfo);
        put(SeatClass.ECONOMY_CLASS.getLabel(), economyInfo);
      }
    };

    List<List<AirplaneSeat>> passengerSeatClass = seatClasses.get(boardingPassesGroup.get(0).getSeatType().getName());
    TravelClass rowClassInfo = rowClassesInfo.get(boardingPassesGroup.get(0).getSeatType().getName());

    List<BoardingPass> boardingPassesWithoutSeats = boardingPassesGroup.stream()
        .filter(boardingPass -> Objects.isNull(boardingPass.getSeat())).collect(Collectors.toList());

    if (boardingPassesWithoutSeats.size() == 0) {
      return boardingPassesGroup;
    }

    List<BoardingPass> boardingPassesWithSeats = boardingPassesGroup.stream()
        .filter(boardingPass -> !Objects.isNull(boardingPass.getSeat())).collect(Collectors.toList());

    Integer initialRowIndex = 0;
    Integer initialColumnIndex = 0;

    if (!boardingPassesWithSeats.isEmpty()) {
      BoardingPass boardingPassWithSeat = boardingPassesWithSeats.get(0);
      Integer row = boardingPassWithSeat.getSeat().getRow();
      String column = boardingPassWithSeat.getSeat().getColumn();

      initialRowIndex = row - rowClassInfo.getRowMinMax().get(0);
      initialColumnIndex = rowClassInfo.getRowNames().indexOf(column);
    }

    List<AirplaneSeat> groupSeats = this.findSeatsToAssign(passengerSeatClass, initialRowIndex, initialColumnIndex,
        boardingPassesWithoutSeats);

    Queue<BoardingPass> underagePassengers = boardingPassesWithoutSeats.stream()
        .filter(boardingPass -> boardingPass.getPassenger().isUnderAge())
        .collect(Collectors.toCollection(LinkedList::new));

    Queue<BoardingPass> adultPassengers = boardingPassesWithoutSeats.stream()
        .filter(boardingPass -> !boardingPass.getPassenger().isUnderAge())
        .collect(Collectors.toCollection(LinkedList::new));

    if (underagePassengers.isEmpty()) {
      for (int i = 0; i < groupSeats.size(); i++) {
        AirplaneSeat seat = groupSeats.get(i);
        BoardingPass boardingPass = boardingPassesWithoutSeats.get(i);
        seat.setAvailable(false);
        seat.setPurchaseId(boardingPass.getPurchase().getId());
        seat.setUnderAge(boardingPass.getPassenger().isUnderAge());

        Seat realSeat = this.getSeatFromAirplaneSeat(boardingPass.getFlight().getAirplane(), seat, rowClassInfo);

        boardingPass.setSeat(realSeat);
        boardingPassesWithSeats.add(boardingPass);
      }
    } else {

      if (!boardingPassesWithSeats.isEmpty()) {
        for (BoardingPass boardingPass : boardingPassesWithSeats) {
          String column = boardingPass.getSeat().getColumn();
          Integer row = boardingPass.getSeat().getRow();
          Integer minRow = rowClassInfo.getRowMinMax().get(0);
          AirplaneSeat seat = passengerSeatClass.get(row - minRow).get(rowClassInfo.getRowNames().indexOf(column));
          groupSeats.add(seat);
        }
      }

      for (int i = 0; i < groupSeats.size(); i++) {
        AirplaneSeat currentSeat = groupSeats.get(i);
        if (currentSeat.isAvailable()) {
          List<AirplaneSeat> adjacentSeats = groupSeats.stream().filter(seat -> seat.isAdjacentTo(currentSeat))
              .collect(Collectors.toList());
          BoardingPass passenger = null;
          if (adjacentSeats.isEmpty()) {
            passenger = adultPassengers.poll();
          } else {
            AirplaneSeat firstAdjacent = adjacentSeats.get(0);

            if (!firstAdjacent.isAvailable() && !firstAdjacent.isUnderAge()) {

              if (underagePassengers.isEmpty()) {
                passenger = adultPassengers.poll();
              } else {
                passenger = underagePassengers.poll();
              }
            } else {
              passenger = adultPassengers.poll();
            }
          }
          currentSeat.setAvailable(false);
          currentSeat.setPurchaseId(passenger.getPurchase().getId());
          currentSeat.setUnderAge(passenger.getPassenger().isUnderAge());

          Seat realSeat = this.getSeatFromAirplaneSeat(passenger.getFlight().getAirplane(), currentSeat, rowClassInfo);

          passenger.setSeat(realSeat);
          boardingPassesWithSeats.add(passenger);
        }
      }

    }

    return boardingPassesWithSeats;
  }

  private Seat getSeatFromAirplaneSeat(Airplane airplane, AirplaneSeat airplaneSeat, TravelClass travelClassInfo) {
    String realColumn = travelClassInfo.getRowNames().get(airplaneSeat.getColumnIndex());

    Integer realRow = travelClassInfo.getRowMinMax().get(0) + airplaneSeat.getRowIndex();

    return airplane.getSeats().stream()
        .filter(seat -> seat.getColumn().equals(realColumn) && seat.getRow().equals(realRow)).findFirst().get();
  }

  private List<AirplaneSeat> findSeatsToAssign(List<List<AirplaneSeat>> passengerSeatClass, Integer initialRow,
      Integer initialColumn,
      List<BoardingPass> boardingPassesWithoutSeats) {

    List<AirplaneSeat> seatsToAssign = new ArrayList<>();

    Long underagePassengers = boardingPassesWithoutSeats.stream()
        .filter(boardingPass -> boardingPass.getPassenger().isUnderAge())
        .count();

    for (int i = initialRow; i < passengerSeatClass.size(); i++) {

      List<AirplaneSeat> seatRow = passengerSeatClass.get(i);

      Boolean rowIsNotFull = seatRow.stream().anyMatch(seat -> seat.isAvailable());
      if (rowIsNotFull) {
        for (int j = initialColumn; j < seatRow.size(); j++) {
          AirplaneSeat currentSeat = seatRow.get(j);

          if (currentSeat.isAvailable()) {
            List<AirplaneSeat> necessarySeats = currentSeat.getAllAvailableInRowGrid(passengerSeatClass);

            if (necessarySeats.size() >= boardingPassesWithoutSeats.size() - 1) {
              necessarySeats = necessarySeats.subList(0,
                  boardingPassesWithoutSeats.size() - 1);
              necessarySeats.add(currentSeat);
              return necessarySeats;
            }

            List<AirplaneSeat> minimumNextSeats = currentSeat.getAllAvailableInRowGrid(passengerSeatClass);

            if ((underagePassengers.intValue() > 0 && minimumNextSeats.size() >= underagePassengers.intValue())
                || underagePassengers.intValue() == 0) {

              if (boardingPassesWithoutSeats.size() % 2 == 0) {
                List<AirplaneSeat> adjacentSeats = currentSeat.getAdjacentSeats(passengerSeatClass);
                if (adjacentSeats.size() + 1 >= boardingPassesWithoutSeats.size()) {
                  seatsToAssign = adjacentSeats.subList(0, boardingPassesWithoutSeats.size() - 1);
                  seatsToAssign.add(currentSeat);
                } else {
                  List<AirplaneSeat> seatsInRow = currentSeat.getAllInRow(passengerSeatClass);
                  if (seatsInRow.size() + 1 >= boardingPassesWithoutSeats.size()) {
                    seatsToAssign = seatsInRow.subList(0, boardingPassesWithoutSeats.size() - 1);
                    seatsToAssign.add(currentSeat);
                  } else if (boardingPassesWithoutSeats.size() > seatsToAssign.size()) {
                    seatsToAssign.add(currentSeat);
                  }
                }
              } else {
                List<AirplaneSeat> seatsInRow = currentSeat.getAllInRow(passengerSeatClass);
                if (seatsInRow.size() + 1 >= boardingPassesWithoutSeats.size()) {
                  seatsToAssign = seatsInRow.subList(0, boardingPassesWithoutSeats.size() - 1);
                  seatsToAssign.add(currentSeat);
                } else {
                  List<AirplaneSeat> adjacentSeats = currentSeat.getAdjacentSeats(passengerSeatClass);
                  if (adjacentSeats.size() + 1 >= boardingPassesWithoutSeats.size()) {
                    seatsToAssign = adjacentSeats.subList(0, boardingPassesWithoutSeats.size() - 1);
                    seatsToAssign.add(currentSeat);
                  } else if (boardingPassesWithoutSeats.size() > seatsToAssign.size()) {
                    seatsToAssign.add(currentSeat);
                  }
                }

              }
            }

          }
        }
      }
    }

    return seatsToAssign;
  }

  public void print() {
    System.out.println("");
    System.out.println("---------PRIMERA CLASE---------");
    for (int i = 0; i < firstClassSeats.size(); i++) {
      List<AirplaneSeat> seatRow = firstClassSeats.get(i);
      StringBuilder sb = new StringBuilder();
      AirplaneSeat previousSeat = null;
      for (AirplaneSeat seat : seatRow) {
        if (seat.isAvailable()) {
          sb.append("----");
        } else {
          sb.append(String.format("%03d", seat.getPurchaseId()).concat(seat.getAgeStatus()));
        }
        sb.append("  ");
        if (seat.getType() == SeatType.AISLE && previousSeat.getType() != SeatType.AISLE) {
          sb.append("  ");
        }
        previousSeat = seat;
      }
      System.out.print(sb.toString());
      System.out.println("");
    }

    System.out.println("");
    System.out.println("---------CLASE ECONOMICA PREMIUM---------");
    for (int i = 0; i < premiumEconomyClassSeats.size(); i++) {
      List<AirplaneSeat> seatRow = premiumEconomyClassSeats.get(i);
      StringBuilder sb = new StringBuilder();
      AirplaneSeat previousSeat = null;
      for (AirplaneSeat seat : seatRow) {
        if (seat.isAvailable()) {
          sb.append("----");
        } else {
          sb.append(String.format("%03d", seat.getPurchaseId()).concat(seat.getAgeStatus()));
        }
        sb.append("  ");
        if (seat.getType() == SeatType.AISLE && previousSeat.getType() != SeatType.AISLE) {
          sb.append("  ");
        }
        previousSeat = seat;
      }
      System.out.print(sb.toString());
      System.out.println("");
    }

    System.out.println("");
    System.out.println("---------CLASE ECONOMICA---------");
    for (int i = 0; i < economyClassSeats.size(); i++) {
      List<AirplaneSeat> seatRow = economyClassSeats.get(i);
      StringBuilder sb = new StringBuilder();
      AirplaneSeat previousSeat = null;
      for (AirplaneSeat seat : seatRow) {
        if (seat.isAvailable()) {
          sb.append("----");
        } else {
          sb.append(String.format("%03d", seat.getPurchaseId()).concat(seat.getAgeStatus()));
        }
        sb.append("  ");
        if (seat.getType() == SeatType.AISLE && previousSeat.getType() != SeatType.AISLE) {
          sb.append("  ");
        }
        previousSeat = seat;
      }
      System.out.print(sb.toString());
      System.out.println("");
    }
  }
}
