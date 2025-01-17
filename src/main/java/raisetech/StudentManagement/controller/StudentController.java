package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service,StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentsList")
  public String getStudentsList(Model model){
    List<Student> students = service.searchStudentsList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentList",converter.convertStudentDetails(students,studentsCourses));

    return "studentList";
  }

  @GetMapping("/studentsCoursesList")
  public List<StudentsCourses> getStudensCoursestList(){
    return service.searchStudentsCourseList();
  }

}
