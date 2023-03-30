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
@Table(name = "flight")
@Data
@NoArgsConstructor
public class Flight {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "flight_id")
  private Long id;

  @Column(name = "takeoff_date_time")
  private Long takeOffDateTime;
  @Column(name = "landing_date_time")
  private Long landingDateTime;

  @Column(name = "takeoff_airport")
  private String takeOffAirport;
  @Column(name = "landing_airport")
  private String landingAirport;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "airplane_id", referencedColumnName = "airplane_id")
  private Airplane airplane;

  @JsonManagedReference
  @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
  private List<BoardingPass> boardingPasses = new ArrayList<>();
}
