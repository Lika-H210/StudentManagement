package raisetech.StudentManagement.service.converter;

import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormalSimple;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormalSimple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  @Test
  void studentとStudentCourseの情報からJSON形式のstudentDetailに期待通りに変換されること()
      throws JsonProcessingException, JSONException {
    Student student1 = createStudentNormalSimple(999);
    Student student2 = createStudentNormalSimple(1000);
    Student student3 = createStudentNormalSimple(1001);

    StudentCourse course1 = createStudentCourseNormalSimple(101, 999);
    StudentCourse course2 = createStudentCourseNormalSimple(102, 999);
    StudentCourse course3 = createStudentCourseNormalSimple(103, 1000);

    List<Student> students = List.of(student1, student2, student3);
    List<StudentCourse> courses = List.of(course1, course2, course3);

    // Act（対象メソッドの呼び出し）
    StudentConverter sut = new StudentConverter();
    List<StudentDetail> actual = sut.convertStudentDetailList(students, courses);

    // 期待されるオブジェクトを生成
    StudentDetail expectedDetail1 = new StudentDetail(student1, List.of(course1, course2));
    StudentDetail expectedDetail2 = new StudentDetail(student2, List.of(course3));
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
}