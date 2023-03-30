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
@Table(name = "purchase")
@Data
@NoArgsConstructor
public class Purchase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "purchase_id")
  private Long id;

  @Column(name = "purchase_date")
  private Integer date;

  @JsonManagedReference
  @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY)
  private List<BoardingPass> boardingPasses = new ArrayList<>();
}
