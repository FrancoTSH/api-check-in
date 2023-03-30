package com.renatofranco.bsalecheckin.util.seatallocator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AirplaneSeatDistribution {
  private TravelClass firstClass;
  private TravelClass premiumEconomyClass;
  private TravelClass economyClass;
}
