---Todo：設定内容の追加
CREATE TABLE IF NOT EXISTS students (
  student_id INT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  full_name_kana VARCHAR(100) NOT NULL,
  nickname VARCHAR(100),
  mail_address VARCHAR(100),
  residence_area VARCHAR(100),
  age INT,
  sex VARCHAR(100),
  remark VARCHAR(1000),
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

---Todo：設定内容の追加
CREATE TABLE IF NOT EXISTS students_courses (
  course_id INT PRIMARY KEY AUTO_INCREMENT,
  student_id INT NOT NULL,
  course VARCHAR(100),
  start_date DATE,
  end_date DATE,
  FOREIGN KEY (student_id) REFERENCES students(student_id)
);

CREATE TABLE course_status (
  course_status_id INT PRIMARY KEY AUTO_INCREMENT,
  course_id INT NOT NULL,
  status VARCHAR(10) NOT NULL,
  provisional_application_date DATE,
  application_date DATE,
  cancel_date DATE,
  CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES students_courses(course_id)
);