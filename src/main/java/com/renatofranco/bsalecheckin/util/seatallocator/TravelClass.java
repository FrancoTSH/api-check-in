package com.renatofranco.bsalecheckin.util.seatallocator;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TravelClass {
  private List<String> rowNames;
  private List<Integer> columnDistribution;
  private List<Integer> rowMinMax;

  public Integer getNumberOfRows() {
    Integer minRow = rowMinMax.get(0);
    Integer maxRow = rowMinMax.get(1);

    return maxRow - minRow + 1;
  }
}
