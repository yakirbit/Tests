package testRunner;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import sequencialRunner.ComputerExpectedOutput;
import sequencialRunner.CourseExpectedOutput;
import sequencialRunner.DepartmentExpectedOutput;
import sequencialRunner.StudentExpectedOutput;

public class SpecificTests {

	private HashMap<String, DepartmentExpectedOutput> departments;
	private HashMap<String, StudentExpectedOutput> students;
	private HashMap<String, CourseExpectedOutput> courses;
	private HashMap<String, ComputerExpectedOutput> computers;
	private HashMap<String, PrivateState> departmentsPrivateStates;
	private HashMap<String, PrivateState> studentsPrivateStates;
	private HashMap<String, PrivateState> coursesPrivateStates;
	private StringBuilder testResults;
	private boolean shouldPrintOutput;

	public SpecificTests(HashMap<String, DepartmentExpectedOutput> departments, HashMap<String, StudentExpectedOutput> students, HashMap<String, CourseExpectedOutput> courses, HashMap<String, ComputerExpectedOutput> computers, HashMap<String, PrivateState> departmentsPrivateStates, HashMap<String, PrivateState> studentsPrivateStates, HashMap<String, PrivateState> coursesPrivateStates) {
		this.departments = departments;
		this.students = students;
		this.courses = courses;
		this.computers = computers;
		this.departmentsPrivateStates = departmentsPrivateStates;
		this.studentsPrivateStates = studentsPrivateStates;
		this.coursesPrivateStates = coursesPrivateStates;
		this.shouldPrintOutput=true;
	}

	public StringBuilder runTests(String testNumber) {
		printHeader(testNumber);
		if (testNumber.equals("12")||testNumber.equals("2"))
			shouldPrintOutput=false;
		testResults = new StringBuilder("");
			switch (testNumber){
				case "1": runTest1(); break;
				case "2": runTest2(); break;
				case "3": runTest3(); break;
				case "4": runTest4(); break;
				case "5": runTest5(); break;
				case "6": runTest6(); break;
				case "7": runTest7(); break;
				case "8": runTest8(); break;
				case "9": runTest9(); break;
				case "10": runTest10(); break;
				case "11": runTest11(); break;
				case "12": runTest12(); break;
				default:
			}
		if (testResults.toString().isEmpty())
			testResults.append("Nice!! All tests passed! \n");
		return testResults;
	}

	private void printHeader(String testNumber) {
		System.out.println("Running test "+testNumber+"..");

	}

	private void runTest1() {
		System.out.println("Checking that both the students have successfully signed up to all courses.");
		validateStudentsRegisteredToCorrectCourses();
	}

	private void validateStudentsRegisteredToCorrectCourses() {
		//check that the students are registered to the correct courses.
		for(String studentName : students.keySet()){
			if (!studentsPrivateStates.containsKey(studentName)) {
				testResults.append("Test failed due to missing students\n");
				return;
			}
			checkStudentIsRegesteredToAllCurrectCourses(studentName);
		}
	}

	private boolean checkStudentIsRegesteredToAllCurrectCourses(String studentName) {
		StudentExpectedOutput studentExpected = students.get(studentName);
		StudentPrivateState student = (StudentPrivateState) studentsPrivateStates.get(studentName);
		HashMap<String, Integer> studentGradeSheet = student.getGrades();
		if (shouldPrintOutput)
			System.out.print("expect "+studentName+" to have successfully registered to the following courses:\n\t");
		for ( String courseExpected : studentExpected.getGradesheet().keySet()) {
			if(shouldPrintOutput)
				System.out.print(courseExpected+", ");
			if (! studentGradeSheet.containsKey(courseExpected)) {
				testResults.append("Test failed since course ").append(courseExpected).append(" is missing from ").append(studentName).append("'s grade sheet \n");
			}
		}
		if(shouldPrintOutput)
			System.out.println("");
		return true;
	}

	private void runTest2() {
		validateStudentsRegisteredToCorrectCourses();

	}

	private void runTest3() {
		//
		System.out.println("validating there are exactly 10 students in both the courses which were opened.");
		validateCoursesHaveCorrectAmountOfRegistered();

	}

	private void validateCoursesHaveCorrectAmountOfRegistered() {
		for(String courseName : courses.keySet()){
			if (!coursesPrivateStates.containsKey(courseName)) {
				testResults.append("Test failed due to missing courses\n");
				return;
			}
			validateCourseHasCurrectAmountOfRegestered(courseName);
		}
	}

	private boolean validateCourseHasCurrectAmountOfRegestered(String courseName) {
		boolean passed=true;
		CourseExpectedOutput expectedCourse = courses.get(courseName);
		String expectedAmoundOfStudents=String.valueOf(expectedCourse.getNumberOfRegestered());
		String expectedAvailableSpots = String.valueOf(expectedCourse.getAvailableSpots());
		System.out.println("validating that "+courseName+" has exactly "+expectedAmoundOfStudents+" students, and "+expectedAvailableSpots+" available spots.");
		CoursePrivateState course = (CoursePrivateState) coursesPrivateStates.get(courseName);
		String amoundOfStudents=String.valueOf(course.getRegistered());
		String availableSpots = String.valueOf(course.getAvailableSpots());
		if ( ! availableSpots.equals(expectedAvailableSpots)) {
			testResults.append("Test failed since course ").append(courseName).append(" should have exactly ").append(expectedAvailableSpots).append(" Available spots, but has ").append(availableSpots).append(" availble spots.\n");
			passed=false;
		}
		if ( ! amoundOfStudents.equals(expectedAmoundOfStudents) ) {
			testResults.append("Test failed since course ").append(courseName).append(" should have exactly ").append(expectedAmoundOfStudents).append(" students, but has ").append(amoundOfStudents).append(" students regestered.\n");
			passed=false;
		}
		return passed;
	}

	private void runTest4() {
		System.out.println("validating that student s1 is regestered to exactly one course");
		validateStudentsRegisteredToCorrectCourses();
	}

	private void runTest5() {
		System.out.println("validating that student s1 is regestered all the courses");
		validateStudentsRegisteredToCorrectCourses();
	}

	private void runTest6() {
		System.out.println("validating that all students are registered to course c0 and are registered to exactly one of courses c1,c2,c3");
		validateCoursesHaveCorrectAmountOfRegistered();
	}

	private void runTest7() {
		System.out.println("Checking that all department's signatures are created using computer1's success sign.");
		for(String studentName : students.keySet()){
			if (!studentsPrivateStates.containsKey(studentName)) {
				testResults.append("Test failed due to missing students!\n");
				return;
			}else if (students.get(studentName).getDepartmentSignature() != -1)
				checkCorrectSignature(studentName);
		}
	}

	private boolean checkCorrectSignature(String studentName) {
		StudentExpectedOutput studentExpected = students.get(studentName);
		StudentPrivateState student = (StudentPrivateState) studentsPrivateStates.get(studentName);
		String expectedSignature = String.valueOf(studentExpected.getDepartmentSignature());
		String singature=String.valueOf(student.getSignature());
		System.out.println("expect student "+studentName+" to have signature "+expectedSignature+".");
		if (!expectedSignature.equals(singature)) {
			testResults.append("Test failed since the signature for student ").append(studentName).append(" was supposed to be ").append(expectedSignature).append(" but is ").append(singature).append(".\n");
			return false;
		}
		return true;
	}

	private void runTest8() {
		System.out.println("Checking all courses were properly closed and no students appear in any course.");
		validateStudentsRegisteredToCorrectCourses();
		validateCoursesHaveCorrectAmountOfRegistered();
	}

	private void runTest9() {
		runTest8();
	}

	private void runTest10() {
		System.out.println("checking all students were properly removed from all courses, and available spots in all courses is 110");
		validateStudentsRegisteredToCorrectCourses();
		validateCoursesHaveCorrectAmountOfRegistered();
	}

	private void runTest11() {
		validateStudentsRegisteredToCorrectCourses();
	}

	private void runTest12() {
		validateStudentsRegisteredToCorrectCourses();
	}


}
