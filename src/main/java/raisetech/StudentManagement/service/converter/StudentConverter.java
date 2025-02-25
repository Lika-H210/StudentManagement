package raisetech.StudentManagement.service.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * コンバーター:受講生及び受講コース情報から受講生詳細情報(またはその逆)に変換します。
 */

@Component
public class StudentConverter {

  /**
   * コンバーター:受講生及び受講コース情報から受講生詳細情報(またはその逆)に変換します。
   *
   * @param students        受講生情報の一覧
   * @param studentsCourses 受講コース情報の一覧
   * @return studentDetail(受講生, 受講生に紐づく受講コース)からなる一覧
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses) {

    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {

      List<StudentsCourses> convertStudentCourses = studentsCourses.stream()
          .filter(studentCourse -> student.getStudentId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());

      studentDetails.add(new StudentDetail(student, convertStudentCourses));
    });
    return studentDetails;
  }
}
