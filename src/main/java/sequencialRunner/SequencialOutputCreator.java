package sequencialRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;

import JsonParsedObjects.Example;
import JsonParsedObjects.Machine;
import JsonParsedObjects.Phase1;
import JsonParsedObjects.Phase2;
import JsonParsedObjects.Phase3;

public class SequencialOutputCreator {

	private HashMap<String,DepartmentExpectedOutput> departments;
    private HashMap<String,StudentExpectedOutput> students;
    private HashMap<String,CourseExpectedOutput> courses;
    private HashMap<String, ComputerExpectedOutput> computers;
    private boolean regestrationPeriod;
    private Example jsonParsed;

	
	public SequencialOutputCreator() {
		regestrationPeriod = true;
	}

	public void ParseJsonInput(String jsonFilename) {
		String text = prepareJson(jsonFilename, "");
		jsonParsed = (new Gson()).fromJson(text, Example.class);
		createInputStates();
		//createJsonExpectedOutput();
	}




	private String prepareJson(String jsonFilename, String text) {
		try {
			text = new String(Files.readAllBytes(Paths.get(jsonFilename)), StandardCharsets.UTF_8);
			return text;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	public  void createInputStates() {
	    departments = new HashMap<String,DepartmentExpectedOutput>();
	    students = new HashMap<String,StudentExpectedOutput>();
	    courses = new HashMap<String,CourseExpectedOutput>();
		createcomputers();
		createPhaseOneExpectedStates();
		createPhaseTwoExpectedStates();
		createPhaseThreeExpectedStates();
	}


	private void createPhaseOneExpectedStates() {
		for (Phase1 parsedData1 : jsonParsed.getPhase1()) {
			switch (parsedData1.getAction()){
				case "Add Student": createAndAddStudent(parsedData1.getDepartment(), parsedData1.getStudent()); break;
				case "Open Course": createAndOpenCourse(parsedData1); break;
				default:
			}
		}
	}


	private void createPhaseTwoExpectedStates() {
		for (Phase2 parsedData2 : jsonParsed.getPhase2()) {
			switch (parsedData2.getAction()){
			case "Add Student": createAndAddStudent(parsedData2.getDepartment(), parsedData2.getStudent()); break;
			case "Participate In Course": participateInCourse(parsedData2.getCourse(), parsedData2.getStudent(), parsedData2.getGrade().get(0)); break;
			case "Close Course": closeCourse(parsedData2.getCourse(), parsedData2.getDepartment());  break;
			case "Add Spaces": addSpaces(parsedData2.getCourse(), parsedData2.getNumber());  break;
			case "End Registeration": endRegistration();  break;
			case "Unregister": unregister(parsedData2.getStudent(), parsedData2.getCourse()); break;
			case "Register With Preferences": regesterWithPref(parsedData2.getGrade(),parsedData2.getPreferences(),parsedData2.getStudent()); break;
			case "Administrative Check": adminCheck(parsedData2.getStudents(),parsedData2.getConditions()); break;
			default: 
			}
		}
	}	
	
	private void createPhaseThreeExpectedStates() {
		for (Phase3 parsedData3 : jsonParsed.getPhase3()) {
			switch (parsedData3.getAction()){
			case "Add Student": createAndAddStudent(parsedData3.getDepartment(), parsedData3.getStudent()); break;
			case "Participate In Course": participateInCourse(parsedData3.getCourse(), parsedData3.getStudent(), parsedData3.getGrade().get(0)); break;
			case "Close Course": closeCourse(parsedData3.getCourse(), parsedData3.getDepartment());  break;
			case "Add Spaces": addSpaces(parsedData3.getCourse(), parsedData3.getNumber());  break;
			case "End Registeration": endRegistration();  break;
			case "Unregister": unregister(parsedData3.getStudent(), parsedData3.getCourse()); break;
			case "Register With Preferences": regesterWithPref(parsedData3.getGrade(),parsedData3.getPreferences(),parsedData3.getStudent()); break;
			case "Administrative Check": adminCheck(parsedData3.getStudents(),parsedData3.getConditions()); break;
			default: 
			}
		}
	}


	private void adminCheck(List<String> studentNames, List<String> conditions) {
		for (String studentName : studentNames) {
			StudentExpectedOutput student = students.get(studentName);
			updateStudentSignature(conditions, student);
		}
	}


	private void updateStudentSignature(List<String> conditions, StudentExpectedOutput student) {
		for (String courseCondition : conditions) {
			if (!student.getGradesheet().containsKey(courseCondition)) {
				student.setDepartmentSignature(computers.get(computers.keySet().toArray()[0]).getSigFail());
				break;
			}
			student.setDepartmentSignature(computers.get(computers.keySet().toArray()[0]).getSigSuccess());
		}
	}


	private void regesterWithPref(List<String> grade, List<String> preferences, String student) {
		List<String> grades = getGrades(grade);
		regesterToOneCourse(preferences, student, grades);
	}


	private void regesterToOneCourse(List<String> preferences, String student, List<String> grades) {
		int ind = 0;
		for (String preference : preferences) {
			boolean regestered = participateInCourse(preference, student, grades.get(ind));
			if (regestered)
				break;
			ind++;
		}
		
	}


	private List<String> getGrades(List<String> grade2) {
		List<String> grades = new ArrayList<String>();
		for (String grade : grade2) {
			grades.add(grade);
		}
		return grades;
	}


	private void unregister(String student1, String course1) {
		if (regestrationPeriod) {
			StudentExpectedOutput student = students.get(student1);
			CourseExpectedOutput course = courses.get(course1);
			student.removeFromGradesheet(course1);
			course.RemoveStudentFromStudentsRegestered(student1);
		}
	}


	private void endRegistration() {
		for (Entry<String, DepartmentExpectedOutput> departmentEntry:departments.entrySet()) {
			HashMap<String,Integer> coursesInDepartment = departmentEntry.getValue().getCourses();
			for(String courseName : coursesInDepartment.keySet()){
				if (courses.get(courseName).getNumberOfRegestered() < 5 ) {
					closeCourse(courseName,departmentEntry.getKey());
				}
			}
		}
		regestrationPeriod = false;
	}


	private void addSpaces(String course1, String number1) {
		CourseExpectedOutput course = courses.get(course1);
		int placesToAdd = Integer.parseInt(number1);
		course.setAvailableSpots(course.getAvailableSpots()+placesToAdd);
	}


	private void closeCourse(String courseName, String departmentName) {
		DepartmentExpectedOutput department = departments.get(departmentName);
		CourseExpectedOutput course = courses.get(courseName);
		for(String studentName : course.getStudentsRegestered().keySet()){
			students.get(studentName).removeFromGradesheet(courseName);
		}
		department.removeFromCourses(courseName);
		course.RemoveAllStudentsRegestered();
		course.setAvailableSpots(-1);
	}


	private boolean participateInCourse(String courseName, String studentName, String grade) {
		StudentExpectedOutput student = students.get(studentName);
		CourseExpectedOutput course = courses.get(courseName);
		return addToCourse(courseName, studentName, grade, student, course);
	}


	private boolean addToCourse(String courseName, String studentName, String grade, StudentExpectedOutput student, CourseExpectedOutput course) {
		if (regestrationPeriod && canRegesterToCourse(student, course, courseName)) {
			student.addToGradesheet(courseName, Integer.parseInt(grade));
			course.addStudentToStudentsRegestered(studentName);
			return true;
		}
		return false;
	}



	private boolean canRegesterToCourse(StudentExpectedOutput student, CourseExpectedOutput course, String courseName) {
		if (course.getAvailableSpots() <= 0) {
			return false;
		}
		List<String> prerequisites = course.getPrerequisites();
		return staisfiesPrerequisites(student, prerequisites);
	}


	private boolean staisfiesPrerequisites(StudentExpectedOutput student, List<String> prerequisites) {
		for (String prerequisite:prerequisites) {
			if (!student.getGradesheet().containsKey(prerequisite)) {
				return false;
			}
		}
		return true;
	}


	private void createAndOpenCourse(Phase1 courseInfo) {
		createDepartment(courseInfo.getDepartment(), "");
		createCourse(courseInfo.getCourse(), Integer.parseInt(courseInfo.getSpace()), courseInfo.getPrerequisites());
	}


	private void createCourse(String courseName, int availableSpots, List<String> prerequisitesList) {
		if (!courses.containsKey(courseName)) {
			CourseExpectedOutput course = new CourseExpectedOutput(availableSpots, 0, new HashMap<String,Integer>(), prerequisitesList);
			courses.put(courseName, course);
		}
	}


	private void createAndAddStudent(String department, String studentname) {
		createStudent(studentname);
		createDepartment(department,studentname);
	}


	private void createStudent(String studentname) {
		if (!students.containsKey(studentname)) {
			long initialSignature = -1;
			StudentExpectedOutput student = new StudentExpectedOutput(initialSignature, new HashMap<String, Integer>());
			students.put(studentname, student);
		}
	}


	private void createDepartment(String department, String studentname) {
		if (!departments.containsKey(department)) {
			DepartmentExpectedOutput addedDepartment = new DepartmentExpectedOutput(new HashMap<String,Integer>(), new HashMap<String,Integer>());
			departments.put(department, addedDepartment);
		}
		if(!studentname.isEmpty())
			departments.get(department).addToStudents(studentname);
	}


	private void createcomputers() {
		computers = new HashMap<String, ComputerExpectedOutput>();
		for (Machine machine : jsonParsed.getComputers()) {
			addComputer(machine);
		}
	}


	private void addComputer(Machine machine) {
		computers.put(machine.getType(), new ComputerExpectedOutput());
		if(machine.getSigSuccess()!=null)
			computers.get(machine.getType()).setSigSuccess(Long.parseLong(machine.getSigSuccess()));
		if(machine.getSigFail()!=null)
			computers.get(machine.getType()).setSigFail(Long.parseLong(machine.getSigFail()));
	}
	
	

    /**
	 * @return the departments
	 */
	public HashMap<String, DepartmentExpectedOutput> getDepartments() {
		return departments;
	}

	/**
	 * @return the students
	 */
	public HashMap<String, StudentExpectedOutput> getStudents() {
		return students;
	}

	/**
	 * @return the courses
	 */
	public HashMap<String, CourseExpectedOutput> getCourses() {
		return courses;
	}

	/**
	 * @return the computers
	 */
	public HashMap<String, ComputerExpectedOutput> getComputers() {
		return computers;
	}



}
