package br.edu.pucminas.matricula.infrastructure.persistence;

import br.edu.pucminas.matricula.domain.model.DisciplineCategory;
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
@Table(name = "discipline")
public class DisciplineEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 120)
  private String name;

  @ManyToOne(optional = false)
  @JoinColumn(name = "course_id", nullable = false)
  private CourseEntity course;

  @ManyToOne(optional = false)
  @JoinColumn(name = "professor_id", nullable = false)
  private ProfessorEntity professor;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false, length = 20)
  private DisciplineCategory category;

  protected DisciplineEntity() {}

  public DisciplineEntity(String name, CourseEntity course, ProfessorEntity professor, DisciplineCategory category) {
    this.name = name;
    this.course = course;
    this.professor = professor;
    this.category = category;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public CourseEntity getCourse() {
    return course;
  }

  public ProfessorEntity getProfessor() {
    return professor;
  }

  public DisciplineCategory getCategory() {
    return category;
  }
}
