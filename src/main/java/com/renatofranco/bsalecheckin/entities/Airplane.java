package com.renatofranco.bsalecheckin.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "airplane")
@Data
@NoArgsConstructor
public class Airplane {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "airplane_id")
  private Long id;

  private String name;

  @JsonManagedReference
  @OneToMany(mappedBy = "airplane")
  private List<Flight> flights = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "airplane")
  private List<Seat> seats = new ArrayList<>();

  @Transient
  public List<String> getFirstClassRowNames() {
    Map<Long, List<String>> rowNames = new HashMap<Long, List<String>>() {
      {
        put(1L, List.of("A", "B", "F", "G"));
        put(2L, List.of("A", "E", "I"));
      }
    };
    return rowNames.get(this.getId());
  }

  @Transient
  public List<String> getPremiumEconomyClassRowNames() {
    Map<Long, List<String>> rowNames = new HashMap<Long, List<String>>() {
      {
        put(1L, List.of("A", "B", "C", "E", "F", "G"));
        put(2L, List.of("A", "B", "D", "E", "F", "H", "I"));
      }
    };
    return rowNames.get(this.getId());
  }

  @Transient
  public List<String> getEconomyClassRowNames() {
    Map<Long, List<String>> rowNames = new HashMap<Long, List<String>>() {
      {
        put(1L, List.of("A", "B", "C", "E", "F", "G"));
        put(2L, List.of("A", "B", "D", "E", "F", "H", "I"));
      }
    };
    return rowNames.get(this.getId());
  }

  @Transient
  public List<Integer> getFirstClassColumnDistribution() {
    Map<Long, List<Integer>> rowNames = new HashMap<Long, List<Integer>>() {
      {
        put(1L, List.of(2, 2));
        put(2L, List.of(1, 1, 1));
      }
    };
    return rowNames.get(this.getId());
  }

  @Transient
  public List<Integer> getPremiumEconomyClassColumnDistribution() {
    Map<Long, List<Integer>> rowNames = new HashMap<Long, List<Integer>>() {
      {
        put(1L, List.of(3, 3));
        put(2L, List.of(2, 3, 2));
      }
    };
    return rowNames.get(this.getId());
  }

  @Transient
  public List<Integer> getEconomyClassColumnDistribution() {
    Map<Long, List<Integer>> rowNames = new HashMap<Long, List<Integer>>() {
      {
        put(1L, List.of(3, 3));
        put(2L, List.of(2, 3, 2));
      }
    };
    return rowNames.get(this.getId());
  }

  @Transient
  public List<Integer> getFirstClassRowMinMax() {
    Map<Long, List<Integer>> rowNames = new HashMap<Long, List<Integer>>() {
      {
        put(1L, List.of(1, 4));
        put(2L, List.of(1, 5));
      }
    };
    return rowNames.get(this.getId());
  }

  @Transient
  public List<Integer> getPremiumEconomyClassRowMinMax() {
    Map<Long, List<Integer>> rowNames = new HashMap<Long, List<Integer>>() {
      {
        put(1L, List.of(8, 15));
        put(2L, List.of(9, 14));
      }
    };
    return rowNames.get(this.getId());
  }

  @Transient
  public List<Integer> getEconomyClassRowMinMax() {
    Map<Long, List<Integer>> rowNames = new HashMap<Long, List<Integer>>() {
      {
        put(1L, List.of(19, 34));
        put(2L, List.of(18, 31));
      }
    };
    return rowNames.get(this.getId());
  }
}
