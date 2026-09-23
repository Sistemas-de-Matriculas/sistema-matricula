package br.edu.pucminas.matricula.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "professor")
public class ProfessorEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private UserEntity user;

  @Column(name = "name", nullable = false, length = 120)
  private String name;

  protected ProfessorEntity() {}

  public ProfessorEntity(UserEntity user, String name) {
    this.user = user;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public UserEntity getUser() {
    return user;
  }

  public String getName() {
    return name;
  }
}
