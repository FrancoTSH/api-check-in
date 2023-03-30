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
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passenger")
@Data
@NoArgsConstructor
public class Passenger {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "passenger_id")
  private Long id;

  private String dni;
  private String name;
  private Integer age;
  private String country;

  @JsonManagedReference
  @OneToMany(mappedBy = "passenger", fetch = FetchType.LAZY)
  private List<BoardingPass> boardingPasses = new ArrayList<>();

  @Transient
  public boolean isUnderAge() {
    return this.age < 18;
  }
}
