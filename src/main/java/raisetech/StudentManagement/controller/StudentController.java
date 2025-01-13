package raisetech.StudentManagement.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.Students;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/studentsList")
  public List<Students> getStudentsList(){
    List<Students> studentAgeLimitedList = new ArrayList<>();
    for(Students student : service.searchStudentsList()){
      if(student.getAge() >= 30 && student.getAge() < 40){
        studentAgeLimitedList.add(student);
      }
    }
    return studentAgeLimitedList;
  }

  @GetMapping("/studentsCoursesList")
  public List<String> getStudensCoursestList(){
    List<String> coursesRelationList = new ArrayList<>();
    for(StudentsCourses studentCourse : service.searchStudentsCourseList()){
        coursesRelationList.add("courseId : " + studentCourse.getCourseId() +", course : " + studentCourse.getCourse());
    }
    return coursesRelationList;
  }

}
