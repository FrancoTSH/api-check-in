package com.renatofranco.bsalecheckin.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PassengerDTO {
  private Long passengerId;
  private Long dni;
  private String name;
  private Integer age;
  private String country;
  private Long boardingPassId;
  private Long purchaseId;
  private Long seatTypeId;
  private Long seatId;
}
