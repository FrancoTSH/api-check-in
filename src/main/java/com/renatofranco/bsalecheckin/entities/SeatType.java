package com.renatofranco.bsalecheckin.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat_type")
@Data
@NoArgsConstructor
public class SeatType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "seat_type_id")
  private Long id;

  private String name;

  @JsonManagedReference
  @OneToMany(mappedBy = "seatType", fetch = FetchType.LAZY)
  private List<Seat> seats = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "seatType", fetch = FetchType.LAZY)
  private List<BoardingPass> boardingPasses = new ArrayList<>();
}
