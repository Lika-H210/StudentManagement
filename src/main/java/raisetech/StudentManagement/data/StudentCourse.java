package raisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity Class: 受講生 (`students_courses`) テーブルのエンティティクラス。 受講コース情報を管理し、データベースと対応しています。
 */
@Getter
@Setter
public class StudentCourse {

  /**
   * コースID (主キー)
   */
  private Integer courseId;

  /**
   * 受講生ID (外部キー: `students` テーブルの `studentId` と紐づく, 必須)
   */
  private Integer studentId;

  /**
   * 受講コース名 (必須)
   */
  private String course;

  /**
   * 受講開始日 (任意)
   */
  private LocalDate startDate;

  /**
   * 受講終了日 (任意)
   */
  private LocalDate endDate;
}
