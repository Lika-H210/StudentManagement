package raisetech.StudentManagement.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static raisetech.StudentManagement.testutil.TestDataFactory.createInitCourseStatus;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormal;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormalSimple;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormal;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormalSimple;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.view.JsonViews;

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
  private Integer courseId1;
  private Integer courseId2;
  private Student student;
  private StudentCourse course1;
  private StudentCourse course2;
  private CourseStatus initCourseStatus1;
  private CourseStatus initCourseStatus2;
  private CourseDetail courseDetail1;
  private CourseDetail courseDetail2;
  private List<CourseDetail> courseDetailList;
  private StudentDetail studentDetail;

  // 各メソッドテスト用に、正常な学生情報とコース情報を持つ初期データを作成
  @BeforeEach
  void setUp() {
    //受講生情報の準備
    studentId = 999;
    student = createStudentNormalSimple(studentId);
    //受講コース情報(リスト)の準備
    courseId1 = 999;
    courseId2 = 9999;
    course1 = createStudentCourseNormalSimple(courseId1, studentId);
    course2 = createStudentCourseNormalSimple(courseId2, studentId);
    //受講コース申込状況(リスト)の準備
    initCourseStatus1 = createInitCourseStatus(999, courseId1);
    initCourseStatus2 = createInitCourseStatus(9999, courseId2);
    //受講コース詳細情報(リスト)の準備
    courseDetail1 = new CourseDetail(course1, initCourseStatus1);
    courseDetail2 = new CourseDetail(course2, initCourseStatus2);
    courseDetailList = List.of(courseDetail1, courseDetail2);
    //受講コース情報の準備
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
    studentDetail.setCourseDetailList(courseDetailList);

    when(service.getStudentDetail(studentId)).thenReturn(studentDetail);

    mockMvc.perform(get("/student/" + studentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.studentId").value(studentId))
        .andExpect(jsonPath("$.student.fullName").value(student.getFullName()))
        .andExpect(jsonPath("$.student.mailAddress").value(student.getMailAddress()))
        .andExpect(jsonPath("$.courseDetailList", hasSize(2)))
        .andExpect(
            jsonPath("$.courseDetailList[0].studentCourse.courseId").value(course1.getCourseId()))
        .andExpect(
            jsonPath("$.courseDetailList[0].studentCourse.course").value(course1.getCourse()))
        .andExpect(jsonPath("$.courseDetailList[0].courseStatus.status").value(
            initCourseStatus1.getStatus()))
        .andExpect(
            jsonPath("$.courseDetailList[1].studentCourse.courseId").value(course2.getCourseId()))
        .andExpect(
            jsonPath("$.courseDetailList[1].studentCourse.course").value(course2.getCourse()))
        .andExpect(jsonPath("$.courseDetailList[1].courseStatus.status").value(
            initCourseStatus2.getStatus()));

    verify(service, times(1)).getStudentDetail(studentId);
  }

  //個人の受講生詳細情報の取得2/正常200
  @Test
  void 受講生詳細情報取得時に受講生情報と空の受講コース情報がJson形式で取得できること()
      throws Exception {
    when(service.getStudentDetail(studentId)).thenReturn(studentDetail);

    mockMvc.perform(get("/student/{studentId}", studentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.studentId").value(studentId))
        .andExpect(jsonPath("$.student.fullName").value(student.getFullName()))
        .andExpect(jsonPath("$.student.mailAddress").value(student.getMailAddress()))
        .andExpect(jsonPath("$.courseDetailList").isEmpty());

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

  //受講生登録処理1
  @Test
  void 受講生登録処理_受講生と受講コース共に登録する場合_serviceが適切に実行されJsonで適切な返り値が返されていること()
      throws Exception {
    Student requestStudent = createStudentNormal(null);
    StudentCourse requestStudentCourse = createStudentCourseNormal(null, null);
    CourseDetail requestCourseDetail = new CourseDetail();
    requestCourseDetail.setStudentCourse(requestStudentCourse);
    requestCourseDetail.setCourseStatus(null);
    StudentDetail requestStudentDetail = new StudentDetail(requestStudent,
        List.of(requestCourseDetail));

    when(service.registerStudentDetail(any())).thenReturn(
        new StudentDetail(student, courseDetailList));

    String jsonStudentDetail = toJsonWithView(requestStudentDetail, JsonViews.OnRegister.class);

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonStudentDetail))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.studentId").isNumber())
        .andExpect(jsonPath("$.student.fullName").value(requestStudent.getFullName()))
        .andExpect(jsonPath("$.courseDetailList", hasSize(2)))
        .andExpect(jsonPath("$.courseDetailList[0].studentCourse.courseId").isNumber())
        .andExpect(jsonPath("$.courseDetailList[0].courseStatus.status").value("仮申込"))
        .andExpect(jsonPath("$.courseDetailList[1].studentCourse.courseId").isNumber())
        .andExpect(jsonPath("$.courseDetailList[1].courseStatus.status").value("仮申込"));
    ;

    verify(service, times(1)).registerStudentDetail(studentDetailCaptor.capture());
  }

  //受講生登録処理2
  @Test
  void 受講生登録処理_受講生のみ登録の場合_serviceが適切に実行されJsonで適切な返り値が返されていること()
      throws Exception {
    //登録処理の正常な
    Student requestStudent = createStudentNormal(null);
    StudentDetail requestStudentDetail = new StudentDetail(requestStudent, null);

    when(service.registerStudentDetail(any())).thenReturn(
        new StudentDetail(student, List.of())  // ← 登録後は空リストを返す想定
    );

    String jsonStudentDetail = toJsonWithView(requestStudentDetail, JsonViews.OnRegister.class);

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonStudentDetail))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.studentId").isNumber())
        .andExpect(jsonPath("$.courseDetailList").isEmpty());

    verify(service, times(1)).registerStudentDetail(studentDetailCaptor.capture());
  }

  //受講生更新処理
  @Test
  void 受講生更新処理が実行できていること() throws Exception {
    studentDetail.setCourseDetailList(courseDetailList);

    String jsonStudentDetail = toJsonWithView(studentDetail, JsonViews.OnAll.class);

    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonStudentDetail))
        .andExpect(status().isOk())
        .andExpect(content().string("正常に更新されました"));

    verify(service, times(1)).updateStudentDetail(studentDetailCaptor.capture());
  }

  // 指定したビュークラスに従って、オブジェクトを JSON 文字列にシリアライズする。
  // 他のテストに影響しないよう、ObjectMapper のコピーを使用。
  private String toJsonWithView(Object obj, Class<?> viewClass) throws JsonProcessingException {
    ObjectMapper localMapper = objectMapper.copy();
    return localMapper
        .setConfig(localMapper.getSerializationConfig().withView(viewClass))
        .writeValueAsString(obj);
  }
}