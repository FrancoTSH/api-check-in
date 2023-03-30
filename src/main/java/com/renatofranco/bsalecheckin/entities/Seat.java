package com.renatofranco.bsalecheckin.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat")
@Data
@NoArgsConstructor
public class Seat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "seat_id")
  private Long id;

  @Column(name = "seat_column", length = 2)
  private String column;

  @Column(name = "seat_row")
  private Integer row;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "airplane_id", referencedColumnName = "airplane_id")
  private Airplane airplane;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seat_type_id", referencedColumnName = "seat_type_id")
  private SeatType seatType;

  @JsonManagedReference
  @OneToMany(mappedBy = "seat", fetch = FetchType.LAZY)
  private List<BoardingPass> boardingPasses = new ArrayList<>();
}
