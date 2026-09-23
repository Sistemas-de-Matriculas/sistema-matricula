package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.OfferingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "offering")
public class OfferingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "semester_id", nullable = false)
  private SemesterEntity semester;

  @ManyToOne(optional = false)
  @JoinColumn(name = "discipline_id", nullable = false)
  private DisciplineEntity discipline;

  @Column(name = "capacity", nullable = false)
  private int capacity;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private OfferingStatus status;

  protected OfferingEntity() {}

  public OfferingEntity(SemesterEntity semester, DisciplineEntity discipline, int capacity, OfferingStatus status) {
    this.semester = semester;
    this.discipline = discipline;
    this.capacity = capacity;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public SemesterEntity getSemester() {
    return semester;
  }

  public DisciplineEntity getDiscipline() {
    return discipline;
  }

  public int getCapacity() {
    return capacity;
  }

  public OfferingStatus getStatus() {
    return status;
  }
}
