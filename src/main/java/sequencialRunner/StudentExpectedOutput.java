package sequencialRunner;
import java.util.HashMap;

public class StudentExpectedOutput{
	private long departmentSignature;
	private HashMap<String,Integer> gradesheet;
	
	
	/**
	 * @param signature
	 * @param gradesheet
	 */
	public StudentExpectedOutput(Long signature, HashMap<String, Integer> gradesheet) {
		this.departmentSignature = signature;
		this.gradesheet = gradesheet;
	}
	
	/**
	 * @return the departmentSignature
	 */
	public long getDepartmentSignature() {
		return departmentSignature;
	}
	/**
	 * @param sig the departmentSignature to set
	 */
	public void setDepartmentSignature(long sig) {
		this.departmentSignature = sig;
	}
	/**
	 * @return the gradesheet
	 */
	public HashMap<String, Integer> getGradesheet() {
		return gradesheet;
	}
	/**
	 * @param courseName and grade the student got in the course
	 */
	public void addToGradesheet(String courseName, int grade) {
		gradesheet.put(courseName, grade);
	}
	/**
	 * @param courseName the courseName
	 */
	public void removeFromGradesheet(String courseName) {
		gradesheet.remove(courseName);
	}

	
	
}
