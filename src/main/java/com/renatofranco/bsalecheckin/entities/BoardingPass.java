package com.renatofranco.bsalecheckin.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "boarding_pass")
@Data
@NoArgsConstructor
public class BoardingPass {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "boarding_pass_id")
  private Long id;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "passenger_id", referencedColumnName = "passenger_id")
  private Passenger passenger;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "purchase_id", referencedColumnName = "purchase_id")
  private Purchase purchase;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "seat_type_id", referencedColumnName = "seat_type_id")
  private SeatType seatType;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "seat_id", referencedColumnName = "seat_id")
  private Seat seat;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "flight_id", referencedColumnName = "flight_id")
  private Flight flight;
}
