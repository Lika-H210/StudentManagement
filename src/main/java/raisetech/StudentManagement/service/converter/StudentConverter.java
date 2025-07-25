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
 * 学生管理システムのデータ変換を行うコンバーターです。 DBから取得した複数のエンティティを組み合わせて、Controller層で扱いやすい形式に変換します。
 */
@Component
public class StudentConverter {

  /**
   * 受講生リストとコース詳細情報リストから受講生詳細情報のリストに変換するconverterです。
   * すべての受講生を対象とし、紐づくコース詳細情報が存在する場合はその情報を、存在しない場合は空のリストを用いて受講生詳細情報を構築します。
   */
  public List<StudentDetail> toStudentDetailFromAllStudents(List<Student> studentList,
      List<CourseDetail> courseDetailList) {

    return studentList.stream()
        .map(student -> new StudentDetail(student,
            courseDetailList.stream()
                .filter(courseDetail -> courseDetail.getStudentCourse().getStudentId()
                    .equals(student.getStudentId()))
                .toList()))
        .toList();
  }

  /**
   * 受講生リストとコース詳細情報リストから受講生詳細情報のリストに変換するconverterです。
   * 受講生の内、studentIdで紐づくコース詳細情報が存在する場合のみStudentDetailに変換しリストに含めます。
   */
  public List<StudentDetail> toStudentDetailFromStudentsWithCourse(
      List<Student> studentList, List<CourseDetail> courseDetailList) {

    Map<Integer, List<CourseDetail>> coursesMap = courseDetailList.stream()
        .collect(
            Collectors.groupingBy(courseDetail -> courseDetail.getStudentCourse().getStudentId()));

    return studentList.stream()
        .filter(student -> coursesMap.containsKey(student.getStudentId()))
        .map(student -> new StudentDetail(student,
            coursesMap.getOrDefault(student.getStudentId(), List.of())))
        .toList();
  }

  /**
   * 受講コースリストとコース申込ステータスリストからコース詳細情報のリストに変換するconverterです。
   * 両リストに同じcourseIdのデータが存在するレコードのみをコース詳細情報に変換しリストに含めます。
   */
  public List<CourseDetail> convertToCourseDetail(List<StudentCourse> studentCourseList,
      List<CourseStatus> courseStatusList) {

    Map<Integer, CourseStatus> statusMap = courseStatusList.stream()
        .collect(Collectors.toMap(CourseStatus::getCourseId, Function.identity()));

    return studentCourseList.stream()
        .filter(studentCourse -> statusMap.containsKey(studentCourse.getCourseId()))
        .map(studentCourse -> new CourseDetail(studentCourse,
            statusMap.get(studentCourse.getCourseId())))
        .toList();
  }

}
