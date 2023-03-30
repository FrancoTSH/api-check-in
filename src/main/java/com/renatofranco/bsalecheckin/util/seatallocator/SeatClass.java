package com.renatofranco.bsalecheckin.util.seatallocator;

public enum SeatClass {
  FIRST_CLASS("Primera clase"), PREMIUM_ECONOMY_CLASS("Clase económica premium"), ECONOMY_CLASS("Clase económica");

  private final String label;

  private SeatClass(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
