package sequencialRunner;
import java.util.HashMap;

public class DepartmentExpectedOutput{
	private HashMap<String,Integer> students;
	private HashMap<String,Integer> courses;

	/**
	 * @param students
	 * @param courses
	 */
	public DepartmentExpectedOutput(HashMap<String, Integer> students, HashMap<String, Integer> courses) {
		this.students = students;
		this.courses = courses;
	}
	
	/**
	 * @return the students
	 */
	public HashMap<String, Integer> getStudents() {
		return students;
	}
	/**
	 * @param students the students to set
	 */
	public void addToStudents(String student) {
		students.put(student, 0);
	}
	/**
	 * @return the courses
	 */
	public HashMap<String, Integer> getCourses() {
		return courses;
	}
	/**
	 * @param courses the courses to set
	 */
	public void addToCourses(String course) {
		courses.put(course, 0);
	}

	/**
	 * @param courses the courses to set
	 */
	public void removeFromCourses(String course) {
		courses.remove(course);
	}
	
	
}
