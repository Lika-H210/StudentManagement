package raisetech.StudentManagement.service.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * converter:受講生と受講コース情報から受講生詳細情報に変換します。
 */

@Component
public class StudentConverter {

  /**
   * 受講生と受講コース情報から受講生詳細情報に変換します。
   *
   * @param studentsList        受講生情報の一覧
   * @param studentsCoursesList 受講コース情報の一覧
   * @return 受講生詳細情報(受講生, 受講生に紐づく受講コース)からなる一覧
   */
  public List<StudentDetail> convertStudentDetailList(List<Student> studentsList,
      List<StudentCourse> studentsCoursesList) {

    return studentsList.stream()
        .map(student -> new StudentDetail(student,
            studentsCoursesList.stream()
                .filter(
                    studentCourse -> student.getStudentId().equals(studentCourse.getStudentId()))
                .collect(Collectors.toList())))
        .collect(Collectors.toList());
  }
}
