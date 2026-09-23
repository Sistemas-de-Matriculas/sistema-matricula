package br.edu.pucminas.matricula.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "course")
public class CourseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, unique = true, length = 120)
  private String name;

  @Column(name = "credits", nullable = false)
  private int credits;

  protected CourseEntity() {}

  public CourseEntity(String name, int credits) {
    this.name = name;
    this.credits = credits;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getCredits() {
    return credits;
  }
}
