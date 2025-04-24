package raisetech.StudentManagement.service.converter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * converter:受講生と受講コース情報から受講生詳細情報に変換します。
 */

@Component
public class StudentConverter {

  /**
   * 受講生と受講コース情報から受講生詳細情報に変換します。
   *
   * @param studentsList     受講生情報の一覧
   * @param courseDetailList 受講コース情報の一覧
   * @return 受講生詳細情報(受講生, 受講生に紐づく受講コース)からなる一覧
   */
  public List<StudentDetail> convertStudentDetailList(List<Student> studentsList,
      List<CourseDetail> courseDetailList) {

    return studentsList.stream()
        .map(student -> new StudentDetail(student,
            courseDetailList.stream()
                .filter(
                    courseDetail -> student.getStudentId()
                        .equals(courseDetail.getStudentCourse().getStudentId()))
                .collect(Collectors.toList())))
        .collect(Collectors.toList());
  }

  /**
   * @param studentsCoursesList
   * @param courseStatusesList
   * @return studentsCoursesListに基づくCourseDetailList
   */
  public List<CourseDetail> convertCourseDetailList(
      List<StudentCourse> studentsCoursesList,
      List<CourseStatus> courseStatusesList) {
    Map<Integer, CourseStatus> statusMap = courseStatusesList.stream()
        .collect(Collectors.toMap(CourseStatus::getCourseId, Function.identity()));

    return studentsCoursesList.stream()
        .map(course -> new CourseDetail(course, statusMap.get(course.getCourseId())))
        .collect(Collectors.toList());
  }
}
