package raisetech.StudentManagement.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生情報（Student）と受講生に紐づく受講コース情報（StudentCourse）をまとめた受講生詳細情報クラスです。 主に画面表示やデータ入出力時のDTOとして使用されます。
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourseList;

}
