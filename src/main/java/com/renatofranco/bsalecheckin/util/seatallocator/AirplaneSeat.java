package com.renatofranco.bsalecheckin.util.seatallocator;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AirplaneSeat {
  private SeatType type;

  @Builder.Default
  private boolean available = true;

  private boolean isUnderAge;
  private Long purchaseId;
  private Integer rowIndex;
  private Integer columnIndex;

  /**
   * 
   * @return true if available is true
   * @return false if available is false
   */
  public Boolean isAvailable() {
    return this.available;
  }

  public String toString() {
    return this.getType().toString();
  }

  public String getAgeStatus() {
    return this.isUnderAge ? "N" : "A";
  }

  public Boolean isAdjacentTo(AirplaneSeat seat) {
    return this.rowIndex == seat.rowIndex && !(this.type == SeatType.AISLE && seat.type == SeatType.AISLE)
        && (this.columnIndex.equals(seat.columnIndex + 1) || this.columnIndex.equals(seat.columnIndex - 1));
  }

  public List<AirplaneSeat> getAllAvailableInRowGrid(List<List<AirplaneSeat>> passengerSeatClass) {
    List<AirplaneSeat> seats = new ArrayList<>();

    for (int i = 1; i <= passengerSeatClass.get(this.rowIndex).size(); i++) {
      if (this.columnIndex + i < passengerSeatClass.get(0).size()) {
        AirplaneSeat midRight = passengerSeatClass.get(this.rowIndex).get(this.columnIndex + i);
        if (!midRight.isAvailable()) {
          break;
        }
        if (midRight.getType() == SeatType.AISLE) {
          seats.add(midRight);
          break;
        }
        seats.add(midRight);
      }
    }

    for (int i = 1; i <= passengerSeatClass.get(this.rowIndex).size(); i++) {
      if (this.columnIndex > i) {
        AirplaneSeat midLeft = passengerSeatClass.get(this.rowIndex).get(this.columnIndex - i);
        if (!midLeft.isAvailable()) {
          break;
        }
        if (midLeft.getType() == SeatType.AISLE) {
          seats.add(midLeft);
          break;
        }
        seats.add(midLeft);
      }
    }

    return seats;
  }

  public List<AirplaneSeat> getAllInRow(List<List<AirplaneSeat>> passengerSeatClass) {
    List<AirplaneSeat> seats = new ArrayList<>();

    for (int i = 1; i <= passengerSeatClass.get(this.rowIndex).size(); i++) {
      if (this.columnIndex + i < passengerSeatClass.get(0).size()) {
        AirplaneSeat midRight = passengerSeatClass.get(this.rowIndex).get(this.columnIndex + i);
        if (!midRight.isAvailable()) {
          break;
        }
        seats.add(midRight);
      }
    }

    for (int i = 1; i <= passengerSeatClass.get(this.rowIndex).size(); i++) {
      if (this.columnIndex > i) {
        AirplaneSeat midLeft = passengerSeatClass.get(this.rowIndex).get(this.columnIndex - i);
        if (!midLeft.isAvailable()) {
          break;
        }
        seats.add(midLeft);
      }
    }

    return seats;
  }

  public List<AirplaneSeat> getAdjacentSeats(List<List<AirplaneSeat>> passengerSeatClass) {
    List<AirplaneSeat> seats = new ArrayList<>();

    if (this.columnIndex > 0) {
      AirplaneSeat midLeft = passengerSeatClass.get(this.rowIndex).get(this.columnIndex - 1);
      if (midLeft.isAvailable()) {
        seats.add(midLeft);
      }
    }

    if (this.columnIndex + 1 < passengerSeatClass.get(0).size()) {
      AirplaneSeat midRight = passengerSeatClass.get(this.rowIndex).get(this.columnIndex + 1);
      if (midRight.isAvailable()) {
        seats.add(midRight);
      }
    }

    if (this.rowIndex > 0) {
      AirplaneSeat top = passengerSeatClass.get(this.rowIndex - 1).get(this.columnIndex);
      if (top.isAvailable()) {
        seats.add(top);
      }
    }

    if (this.rowIndex + 1 < passengerSeatClass.size()) {
      AirplaneSeat bottom = passengerSeatClass.get(this.rowIndex + 1).get(this.columnIndex);
      if (bottom.isAvailable()) {
        seats.add(bottom);
      }
    }

    if (this.rowIndex > 0 && this.columnIndex > 0) {
      AirplaneSeat topLeft = passengerSeatClass.get(this.rowIndex - 1).get(this.columnIndex - 1);
      if (topLeft.isAvailable()) {
        seats.add(topLeft);
      }
    }

    if (this.rowIndex > 0 && this.columnIndex + 1 < passengerSeatClass.get(0).size()) {
      AirplaneSeat topRight = passengerSeatClass.get(this.rowIndex - 1).get(this.columnIndex + 1);
      if (topRight.isAvailable()) {
        seats.add(topRight);
      }
    }

    if (this.rowIndex + 1 < passengerSeatClass.size() && this.columnIndex > 0) {
      AirplaneSeat bottomLeft = passengerSeatClass.get(this.rowIndex + 1).get(this.columnIndex - 1);
      if (bottomLeft.isAvailable()) {
        seats.add(bottomLeft);
      }
    }

    if (this.rowIndex + 1 < passengerSeatClass.size() && this.columnIndex + 1 < passengerSeatClass.get(0).size()) {
      AirplaneSeat bottomRight = passengerSeatClass.get(this.rowIndex + 1).get(this.columnIndex + 1);
      if (bottomRight.isAvailable()) {
        seats.add(bottomRight);
      }
    }

    return seats;
  }
}
