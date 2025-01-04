package raisetech.StudentManagement;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	private Map<String,Integer> studentInfoMap = new HashMap<>();

	public StudentManagementApplication() {
		// デフォルト値を追加
		studentInfoMap.put("Yamada Taro", 20);
		studentInfoMap.put("Tanaka Hanako", 22);
	}

		@Autowired
		private StudentRepository repository;


	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentInfoTable")
	public String getStudentList(){
		List<Student> students = repository.getStudentList();
		return students.stream()
				.map(Student::toString)
				.collect(Collectors.joining("\n"));
	}

	@GetMapping("/studentInfo")
	public String getStudentInfo(@RequestParam String name){
		Student student = repository.searchByName(name);
		return  student.getName() + ", " + student.getAge() + "歳, " + student.getCourse();
	}

	@PostMapping("/studentInfo")
	public void registerStudent(String name, int age,String course) {
		repository.registerStudent(name,age,course);
	}

	@PatchMapping("/studentInfo")
	public void updateStudentAge(String name, int age) {
		repository.updateStudentAge(name,age);
	}

	@PatchMapping("/studentInfoCourse")
	public void updateStudentCourse(String name, String course) {
		repository.updateStudentCourse(name,course);
	}

	@DeleteMapping("/studentInfo")
	public void deleteStudent(String name) {
		repository.deleteStudent(name);
	}

	@GetMapping("/studentInfoMap")
	public Map<String, Integer> getStudentInfoMap(){
		return  studentInfoMap;
	}

	@PostMapping("/addStudentMap")
	public String addStudent(@RequestParam String name, @RequestParam Integer age) {
		// リストをMapに追加
		studentInfoMap.put(name, age);
		return "Student added:name " + name + " age " + age;
	}

	@PutMapping("/updateAge")
	public String updateAge(@RequestParam String name, @RequestParam Integer age) {
		if (studentInfoMap.containsKey(name)) {
			studentInfoMap.put(name, age);
			return "Updated " + name + "'s age to " + age;
		}
		return "Student " + name + " not found!";
	}

	@PutMapping("/updateName")
	public String updateName(@RequestParam String oldName, @RequestParam String newName) {
		if (studentInfoMap.containsKey(oldName)) {
			Integer age = studentInfoMap.remove(oldName);
			studentInfoMap.put(newName, age);
			return "Updated name from " + oldName + " to " + newName;
		}
		return "Student " + oldName + " not found!";
	}

}

