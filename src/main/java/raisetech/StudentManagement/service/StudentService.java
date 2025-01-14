package raisetech.StudentManagement.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Students;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Students> searchStudentsList(){
    List<Students> studentAgeLimitedList = new ArrayList<>();
    for(Students student : repository.searchStudents()){
      if(student.getAge() >= 30 && student.getAge() < 40){
        studentAgeLimitedList.add(student);
      }
    }
    return studentAgeLimitedList;
  }

  public List<String> searchStudentsCourseList(){
    List<String> coursesRelationList = new ArrayList<>();
    for(StudentsCourses studentCourse : repository.searchStudentsCourses()){
      coursesRelationList.add("courseId : " + studentCourse.getCourseId() +", course : " + studentCourse.getCourse());
    }
    return coursesRelationList;
  }

}
