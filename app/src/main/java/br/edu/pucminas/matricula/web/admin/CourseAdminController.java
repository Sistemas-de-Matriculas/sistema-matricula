package br.edu.pucminas.matricula.web.admin;

import br.edu.pucminas.matricula.application.usecase.CourseResponse;
import br.edu.pucminas.matricula.application.usecase.CreateCourseRequest;
import br.edu.pucminas.matricula.application.usecase.CreateCourseUseCase;
import br.edu.pucminas.matricula.application.usecase.DeleteCourseUseCase;
import br.edu.pucminas.matricula.application.usecase.ListCoursesUseCase;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/courses")
@PreAuthorize("hasRole('SECRETARIA')")
public class CourseAdminController {
  private final ListCoursesUseCase listCoursesUseCase;
  private final CreateCourseUseCase createCourseUseCase;
  private final DeleteCourseUseCase deleteCourseUseCase;

  public CourseAdminController(
      ListCoursesUseCase listCoursesUseCase,
      CreateCourseUseCase createCourseUseCase,
      DeleteCourseUseCase deleteCourseUseCase) {
    this.listCoursesUseCase = listCoursesUseCase;
    this.createCourseUseCase = createCourseUseCase;
    this.deleteCourseUseCase = deleteCourseUseCase;
  }

  @GetMapping
  public List<CourseResponse> list() {
    return listCoursesUseCase.execute();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CourseResponse create(@RequestBody CreateCourseRequest request) {
    return createCourseUseCase.execute(request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
    deleteCourseUseCase.execute(id);
  }
}
