package raisetech.StudentManagement.validation;

public interface StudentValidation {

  /**
   * 新規受講生登録時のvalidation (受講コース情報の同時登録時も含む）
   */
  public interface OnRegisterStudent {

  }

  /**
   * 新規受講コース登録時のvalidation (コースのみの登録)
   */
  public interface OnRegisterCourse {

  }

  /**
   * 受講生及び受講コース更新時のvalidation
   */
  public interface OnUpdate {

  }

}
