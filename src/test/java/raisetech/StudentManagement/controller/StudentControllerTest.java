package raisetech.StudentManagement.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Captor
  private ArgumentCaptor<StudentDetail> studentDetailCaptor;

  private Integer studentId;
  private Student student;
  private StudentCourse course1;
  private StudentCourse course2;
  private List<StudentCourse> studentCourseList;
  private StudentDetail studentDetail;

  // 各メソッドテスト用に、正常な学生情報とコース情報を持つ初期データを作成
  @BeforeEach
  void setUp() {
    studentId = 999;
    student = TestDataFactory.createStudentNormal(studentId);
    course1 = TestDataFactory.createStudentCourseNormal(999, studentId);
    course2 = TestDataFactory.createStudentCourseNormal(9999, studentId);
    studentCourseList = List.of(course1, course2);
    studentDetail = new StudentDetail(student, Collections.emptyList());
  }

  //受講生詳細情報一覧の取得1
  @Test
  void 受講生詳細の一覧検索が実行できていること() throws Exception {
    mockMvc.perform(get("/studentsList"))
        .andExpect(status().isOk());

    verify(service, times(1)).getStudentDetailList();
  }

  //個人の受講生詳細情報の取得1/正常200
  @Test
  void 受講生詳細情報取得時に受講生情報と受講コース情報がJson形式で取得できること()
      throws Exception {
    studentDetail.setStudentCourseList(studentCourseList);

    when(service.getStudentDetail(studentId)).thenReturn(studentDetail);

    mockMvc.perform(get("/student/" + studentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.studentId").value(studentId))
        .andExpect(jsonPath("$.student.fullName").value(student.getFullName()))
        .andExpect(jsonPath("$.student.mailAddress").value(student.getMailAddress()))
        .andExpect(jsonPath("$.studentCourseList", hasSize(2)))
        .andExpect(jsonPath("$.studentCourseList[0].courseId").value(course1.getCourseId()))
        .andExpect(jsonPath("$.studentCourseList[0].course").value(course1.getCourse()))
        .andExpect(jsonPath("$.studentCourseList[1].courseId").value(course2.getCourseId()))
        .andExpect(jsonPath("$.studentCourseList[1].course").value(course2.getCourse()));

    verify(service, times(1)).getStudentDetail(studentId);
  }

  //個人の受講生詳細情報の取得2/正常200
  @Test
  void 受講生詳細情報取得時に受講生情報と空の受講コース情報がJson形式で取得できること()
      throws Exception {
    when(service.getStudentDetail(studentId)).thenReturn(studentDetail);

    mockMvc.perform(get("/student/" + studentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.studentId").value(studentId))
        .andExpect(jsonPath("$.student.fullName").value(student.getFullName()))
        .andExpect(jsonPath("$.student.mailAddress").value(student.getMailAddress()))
        .andExpect(jsonPath("$.studentCourseList").isEmpty());

    verify(service, times(1)).getStudentDetail(studentId);
  }

  // 個人の受講生詳細情報の取得3/異常404 NOT FOUND
  @Test
  void 未登録のstudentIdで受講生情報取得した場合にResponseStatusExceptionの404エラーメッセージとなる()
      throws Exception {
    when(service.getStudentDetail(999))
        .thenThrow(
            new ResponseStatusException(HttpStatus.NOT_FOUND, "受講生が見つかりませんでした"));

    mockMvc.perform(get("/student/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
        .andExpect(jsonPath("$.error").value("受講生が見つかりませんでした"));
  }

  //受講生登録処理
  @Test
  void 受講生登録処理が実行できていること() throws Exception {
    //登録処理の正常な
    Student student = TestDataFactory.createStudentNormal(null);
    StudentCourse studentCourse = TestDataFactory.createStudentCourseNormal(null, null);
    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudentDetail(studentDetailCaptor.capture());
  }

  //受講生更新処理
  @Test
  void 受講生更新処理が実行できていること() throws Exception {
    studentDetail.setStudentCourseList(studentCourseList);

    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudentDetail(studentDetailCaptor.capture());
  }

}