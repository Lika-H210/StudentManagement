
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

CREATE TABLE IF NOT EXISTS students_courses (
  course_id INT PRIMARY KEY AUTO_INCREMENT,
  student_id INT NOT NULL,
  course VARCHAR(100),
  start_date DATE,
  end_date DATE,
  FOREIGN KEY (student_id) REFERENCES students(student_id)
);