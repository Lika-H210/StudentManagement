package raisetech.StudentManagement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	@Autowired
	private StudentRepository repository;


	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/students")
	public List<Students> getStudentsList(){
		return repository.getStudentsList();
	}

	@GetMapping("/studentsCourses")
	public List<StudentsCourses> getStudensCoursestList(){
		return repository.getStudentsCoursesList();
	}

}

