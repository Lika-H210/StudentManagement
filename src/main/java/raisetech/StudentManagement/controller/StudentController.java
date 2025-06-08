package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.date.Student;
import raisetech.StudentManagement.date.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> studentList = service.searchStudentList();
    List<StudentCourse> studentCourseList = service.searchStudentCourseList();
    List<StudentDetail> studentDetailList = converter.convertToStudentDetail(studentList,
        studentCourseList);
    model.addAttribute("studentList", studentDetailList);
    return "studentList";
  }

  @GetMapping("/student/{publicId}")
  public String getStudentByPublicId(@PathVariable String publicId, Model model) {
    StudentDetail studentDetail = service.searchStudentDetailByPublicId(publicId);
    model.addAttribute("studentDetail", studentDetail);
    return "student";
  }

  @GetMapping("/student/{publicId}/edit")
  public String editStudent(@PathVariable String publicId, Model model) {
    StudentDetail studentDetail = service.searchStudentDetailByPublicId(publicId);
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail,
      RedirectAttributes redirectAttributes) {
    service.updateStudentDetail(studentDetail);
    redirectAttributes.addAttribute("publicId", studentDetail.getStudent().getPublicId());
    return "redirect:/student/{publicId}";
  }

  @GetMapping("/newStudent")
  public String displayRegisterStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    //Todo:バリデーションerror時の動作を入れる（バリデーション確認の機能実装後対応）
    service.registerStudentDetail(studentDetail);
    return "redirect:/studentList";
  }

}
