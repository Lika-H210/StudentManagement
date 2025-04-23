package raisetech.StudentManagement.service.converter;

import static raisetech.StudentManagement.testutil.TestDataFactory.createInitCourseStatus;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormalSimple;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormalSimple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private Student student1, student2, student3;
  private StudentCourse course1, course2, course3;
  private CourseStatus status1, status2, status3;
  private CourseDetail courseDetail1, courseDetail2, courseDetail3;

  @BeforeEach
  void setUp() {
    student1 = createStudentNormalSimple(999);
    student2 = createStudentNormalSimple(1000);
    student3 = createStudentNormalSimple(1001);

    course1 = createStudentCourseNormalSimple(101, 999);
    course2 = createStudentCourseNormalSimple(102, 999);
    course3 = createStudentCourseNormalSimple(103, 1000);

    status1 = createInitCourseStatus(101, 101);
    status2 = createInitCourseStatus(102, 102);
    status3 = createInitCourseStatus(103, 103);

    courseDetail1 = new CourseDetail(course1, status1);
    courseDetail2 = new CourseDetail(course2, status2);
    courseDetail3 = new CourseDetail(course3, status3);
  }

  @Test
  void studentとCourseDetailの情報からJSON形式のstudentDetailに期待通りに変換されること()
      throws JsonProcessingException, JSONException {
    List<Student> students = List.of(student1, student2, student3);
    List<CourseDetail> courseDetails = List.of(courseDetail1, courseDetail2, courseDetail3);

    // Act（対象メソッドの呼び出し）
    StudentConverter sut = new StudentConverter();
    List<StudentDetail> actual = sut.convertStudentDetailList(students, courseDetails);

    // 期待されるオブジェクトを生成
    StudentDetail expectedDetail1 = new StudentDetail(student1,
        List.of(courseDetail1, courseDetail2));
    StudentDetail expectedDetail2 = new StudentDetail(student2, List.of(courseDetail3));
    StudentDetail expectedDetail3 = new StudentDetail(student3, List.of());
    List<StudentDetail> expected = List.of(expectedDetail1, expectedDetail2, expectedDetail3);
    // JSON比較
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    String actualJson = mapper.writeValueAsString(actual);
    String expectedJson = mapper.writeValueAsString(expected);

    JSONAssert.assertEquals(expectedJson, actualJson, true);
  }

  @Test
  void StudentCourseのListに紐づくCourseStatusを適切にJSON形式のCourseDetailに変換できていること()
      throws JsonProcessingException, JSONException {
    List<StudentCourse> studentCourses = List.of(course1, course2, course3);
    List<CourseStatus> courseStatuses = List.of(status1, status2, status3);

    // Act
    StudentConverter sut = new StudentConverter();
    List<CourseDetail> actual = sut.convertCourseDetailList(studentCourses, courseStatuses);

    // Assert - 期待される結果を準備
    List<CourseDetail> expected = List.of(
        new CourseDetail(course1, status1),
        new CourseDetail(course2, status2),
        new CourseDetail(course3, status3)
    );

    // JSON比較
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    String actualJson = mapper.writeValueAsString(actual);
    String expectedJson = mapper.writeValueAsString(expected);

    JSONAssert.assertEquals(expectedJson, actualJson, true);
  }
}