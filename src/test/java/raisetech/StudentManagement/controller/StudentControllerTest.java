package raisetech.StudentManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.converter.ErrorResponseConverter;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.testdata.TestDataFactory;

/**
 * StudentControllerのリクエストの処理を検証するためのテストクラスです。
 */
@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  @MockitoBean
  private ErrorResponseConverter errorResponseConverter;

  //受講生全件検索：正常系
  @Test
  void 受講生全件検索実行時に正常に処理が実行され適切なServiceが呼び出されていること()
      throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentDetailList();
  }

  //受講生個人検索：正常系
  @Test
  void 受講生個人検索実行時に正常に処理が実行され適切なServiceが呼び出されていること()
      throws Exception {
    String publicId = "00000000-0000-0000-0000-000000000000";
    mockMvc.perform(get("/student/{publicId}", publicId))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentDetailByPublicId(publicId);
  }

  //受講生詳細情報登録処理：正常系
  @Test
  void 受講生詳細の登録時に正常に処理が実行され適切なServiceが呼び出されていること()
      throws Exception {
    TestDataFactory createData = new TestDataFactory();
    Student student = createData.createBaseStudentForRegister();
    StudentCourse studentCourse = createData.createBaseCourseForRegister();
    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));

    String jsonStudentDetail = new ObjectMapper().writeValueAsString(studentDetail);

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonStudentDetail))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudentDetail(any(StudentDetail.class));
  }

  //受講生詳細情報更新処理：正常系
  @Test
  void 受講生詳細の更新時に正常に処理が実行され適切なServiceが呼び出されていること()
      throws Exception {
    TestDataFactory createData = new TestDataFactory();
    Student student = createData.createBaseStudent();
    StudentCourse studentCourse = createData.createBaseCourse();
    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));

    String jsonStudentDetail = new ObjectMapper().writeValueAsString(studentDetail);

    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonStudentDetail))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudentDetail(any(StudentDetail.class));
  }

  //異常系
  @Test
  void 実装していないURLを入力された場合に404エラーとなること()
      throws Exception {
    mockMvc.perform(get("/XXX"))
        .andExpect(status().isNotFound());
  }
}
