package com.renatofranco.bsalecheckin.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import com.renatofranco.bsalecheckin.dtos.FlightDTO;
import com.renatofranco.bsalecheckin.entities.Airplane;
import com.renatofranco.bsalecheckin.entities.Flight;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FlightMapper {

  @Mappings({
      @Mapping(target = "id", source = "flightDTO.flightId"),
      @Mapping(target = "airplane", ignore = true),
      @Mapping(target = "boardingPasses", ignore = true)
  })
  Flight toEntity(FlightDTO flightDTO);

  @Mappings({
      @Mapping(target = "flightId", source = "flight.id"),
      @Mapping(target = "airplaneId", source = "airplane.id"),
      @Mapping(target = "passengers", ignore = true),
  })
  FlightDTO toDto(Flight flight, Airplane airplane);
}
