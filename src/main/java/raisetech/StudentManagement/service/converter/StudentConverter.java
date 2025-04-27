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
   * 受講コース一覧に基づく受講生詳細情報に変換します。
   *
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

  /**
   * 受講コース一覧とコース申込状況一覧からどちらにも同じcourseIdがある組のみをCourseDetailに変換し一覧化ます。
   *
   * @param studentCourseList
   * @param courseStatusList
   * @return studentsCoursesListに基づくCourseDetailList
   */
  public List<CourseDetail> combineStudentCourseWithCourseStatusByCourseId(
      List<StudentCourse> studentCourseList,
      List<CourseStatus> courseStatusList) {
    Map<Integer, CourseStatus> courseStatusMap = courseStatusList.stream()
        .collect(Collectors.toMap(CourseStatus::getCourseId, Function.identity()));

    return studentCourseList.stream()
        .filter(studentCourse -> courseStatusMap.containsKey(studentCourse.getCourseId()))
        .map(studentCourse -> new CourseDetail(studentCourse,
            courseStatusMap.get(studentCourse.getCourseId())))
        .collect(Collectors.toList());
  }

  /**
   * 受講コース一覧とコース申込状況一覧からどちらにも同じcourseIdがある組のみをCourseDetailに変換し一覧化ます。
   *
   * @param studentList
   * @param courseDetailList
   * @return studentsCoursesListに基づくCourseDetailList
   */
  public List<StudentDetail> combineStudentsWithCourseDetailsByStudentId(List<Student> studentList,
      List<CourseDetail> courseDetailList) {
    Map<Integer, List<CourseDetail>> courseDetailMap = courseDetailList.stream()
        .collect(Collectors.groupingBy(cd -> cd.getStudentCourse().getStudentId()));

    return studentList.stream()
        .filter(student -> courseDetailMap.containsKey(student.getStudentId()))
        .map(student -> new StudentDetail(student, courseDetailMap.get(student.getStudentId())))
        .collect(Collectors.toList());
  }
}
