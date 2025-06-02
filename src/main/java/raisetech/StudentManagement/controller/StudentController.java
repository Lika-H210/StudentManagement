package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.date.Student;
import raisetech.StudentManagement.date.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> studentList = service.searchStudentList();
    List<StudentCourse> studentCourseList = service.searchStudentCourseList();
    List<StudentDetail> studentDetailList = converter.convertToStudentDetail(studentList,
        studentCourseList);
    model.addAttribute("studentList", studentDetailList);
    return "studentList";
  }

  //todo:引数をpublicIdに変更する
  @GetMapping("/student")
  public Student getStudentById(@RequestParam Integer studentId) {
    return service.searchStudentById(studentId);
  }

}
