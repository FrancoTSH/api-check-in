package com.renatofranco.bsalecheckin.mappers;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import com.renatofranco.bsalecheckin.dtos.PassengerDTO;
import com.renatofranco.bsalecheckin.entities.BoardingPass;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

  @IterableMapping(qualifiedByName = "SinglePassenger")
  List<PassengerDTO> toDtoList(List<BoardingPass> boardingPasses);

  @Named("SinglePassenger")
  default PassengerDTO toDto(BoardingPass boardingPass) {
    return PassengerDTO.builder()
        .passengerId(boardingPass.getPassenger().getId())
        .dni(Long.parseLong(boardingPass.getPassenger().getDni()))
        .name(boardingPass.getPassenger().getName())
        .age(boardingPass.getPassenger().getAge())
        .country(boardingPass.getPassenger().getCountry())
        .boardingPassId(boardingPass.getId())
        .purchaseId(boardingPass.getPurchase().getId())
        .seatTypeId(boardingPass.getSeatType().getId())
        .seatId(boardingPass.getSeat().getId()).build();
  }
}
