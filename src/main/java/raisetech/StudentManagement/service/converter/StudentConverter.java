package raisetech.StudentManagement.service.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.date.Student;
import raisetech.StudentManagement.date.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

@Component
public class StudentConverter {

  public List<StudentDetail> convertToStudentDetail(List<Student> studentList,
      List<StudentCourse> studentCourseList) {

    return studentList.stream()
        .map(student -> new StudentDetail(student,
            studentCourseList.stream()
                .filter(
                    studentCourse -> studentCourse.getStudentId().equals(student.getStudentId()))
                .toList()))
        .toList();
  }

}
