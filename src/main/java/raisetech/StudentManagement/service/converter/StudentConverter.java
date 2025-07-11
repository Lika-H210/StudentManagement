package raisetech.StudentManagement.service.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生情報（Student）および受講コース情報（StudentCourse）を、
 * Controllerで扱いやすい受講生詳細情報（StudentDetail）の形式に変換するコンバーターです。
 */
@Component
public class StudentConverter {

  public List<StudentDetail> convertToStudentDetail(List<Student> studentList,
      List<CourseDetail> courseDetailList) {

    return studentList.stream()
        .map(student -> new StudentDetail(student,
            courseDetailList.stream()
                .filter(courseDetail -> courseDetail.getStudentCourse().getStudentId()
                    .equals(student.getStudentId()))
                .toList()))
        .toList();
  }

}
