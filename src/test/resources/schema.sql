CREATE TABLE students (
  student_id INT PRIMARY KEY AUTO_INCREMENT,
  public_id VARCHAR(36) NOT NULL,
  full_name VARCHAR(50) NOT NULL,
  kana_name VARCHAR(50) NOT NULL,
  nickname VARCHAR(50),
  email VARCHAR(50) NOT NULL,
  region VARCHAR(50),
  age INT,
  sex VARCHAR(10),
  remark VARCHAR(1000) DEFAULT '',
  is_deleted BOOLEAN DEFAULT FALSE,
  UNIQUE (public_id),
  UNIQUE (email)
);

CREATE TABLE students_courses (
  course_id INT PRIMARY KEY AUTO_INCREMENT,
  student_id INT NOT NULL,
  course VARCHAR(30) NOT NULL,
  start_date DATE,
  end_date DATE,
  FOREIGN KEY (student_id) REFERENCES students(student_id)
);

CREATE TABLE course_status (
  status_id INT PRIMARY KEY AUTO_INCREMENT,
  course_id INT NOT NULL,
  status VARCHAR(10) NOT NULL,
  provisional_application_date DATE,
  application_date DATE,
  cancel_date DATE,
  UNIQUE (course_id),
  FOREIGN KEY (course_id) REFERENCES students_courses(course_id)
);
