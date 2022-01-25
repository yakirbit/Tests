package testRunner;

import java.util.HashMap;
import java.util.Map.Entry;

import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.*;
import sequencialRunner.ComputerExpectedOutput;
import sequencialRunner.CourseExpectedOutput;
import sequencialRunner.DepartmentExpectedOutput;
import sequencialRunner.StudentExpectedOutput;

public class GeneralTests {

	private HashMap<String,DepartmentExpectedOutput> departments;
    private HashMap<String,StudentExpectedOutput> students;
    private HashMap<String,CourseExpectedOutput> courses;
    private HashMap<String, ComputerExpectedOutput> computers;
    private HashMap<String,PrivateState> departmentsPrivateStates;
    private HashMap<String,PrivateState> studentsPrivateStates;
    private HashMap<String,PrivateState> coursesPrivateStates;
    private StringBuilder testResults;

    /**
	 * @param departments
	 * @param students
	 * @param courses
	 * @param computers
	 * @param departmentsPrivateStates
	 * @param studentsPrivateStates
	 * @param coursesPrivateStates
	 */
	public GeneralTests(HashMap<String, DepartmentExpectedOutput> departments,
			HashMap<String, StudentExpectedOutput> students, HashMap<String, CourseExpectedOutput> courses,
			HashMap<String, ComputerExpectedOutput> computers, HashMap<String, PrivateState> departmentsPrivateStates,
			HashMap<String, PrivateState> studentsPrivateStates, HashMap<String, PrivateState> coursesPrivateStates) {
		this.departments = departments;
		this.students = students;
		this.courses = courses;
		this.computers = computers;
		this.departmentsPrivateStates = departmentsPrivateStates;
		this.studentsPrivateStates = studentsPrivateStates;
		this.coursesPrivateStates = coursesPrivateStates;
	}


	public StringBuilder runTests() {
		testResults = new StringBuilder("");
		runGeneralTests();
		return testResults;
	}


	private void runGeneralTests() {
		runStudentTests();
		runDepartemtTests();
		runCoursesTests();
	}


	private void runCoursesTests() {
		for (Entry<String, CourseExpectedOutput> courseEntry:courses.entrySet()) {
			String courseName = courseEntry.getKey();
			CourseExpectedOutput course = courseEntry.getValue();
			PrivateState coursePrivateState = coursesPrivateStates.get(courseName);
			//course tests 1.check that The list of prerequisites as in the json file
			checkCoursePrerequisites(courseName, course, coursePrivateState);
			//course tests 2.check that number of registered students does not exceed  the  number  of spaces in the course.
			//course tests 3.check that the registered students have the correct  prerequisites.
			checkCourseRegisteredStudents(courseName, course, coursePrivateState);

		}
	}



	private void checkCoursePrerequisites(String courseName, CourseExpectedOutput course, PrivateState coursePrivateState) {
		int ind = 0;
		for (String prerequisite:course.getPrerequisites()) {
			String coursePrivateStatePrerequisites = ((CoursePrivateState) coursePrivateState).getPrequisites().get(ind);
			if (!prerequisite.equalsIgnoreCase(coursePrivateStatePrerequisites)) {
				createCoursePrerequisitesError(courseName, course, coursePrivateStatePrerequisites);
				break;
			}
			ind++;
		}
	}


	private void checkCourseRegisteredStudents(String courseName, CourseExpectedOutput course, PrivateState coursePrivateState) {
		if (course.getAvailableSpots()!=-1 && course.getAvailableSpots()+course.getNumberOfRegestered() < ((CoursePrivateState) coursePrivateState).getRegistered()) {
			testResults.append("####GENERAL-TESTS-ERROR#####\n too many students: ");
			testResults.append(courseName);
			testResults.append(" should have: ");
			testResults.append(String.valueOf(course.getAvailableSpots()+course.getNumberOfRegestered()));
			testResults.append("spots. But has: ");
			testResults.append(String.valueOf(((CoursePrivateState) coursePrivateState).getRegistered()));
			testResults.append(" students registered.\n");
		}

	}


	private void createCoursePrerequisitesError(String courseName, CourseExpectedOutput course,String coursePrivateStatePrerequisites) {
		testResults.append("####GENERAL-TESTS-ERROR#####\nprerequisites for course ");
		testResults.append(courseName);
		testResults.append(" should be: ");
		testResults.append(course.getPrerequisites());
		testResults.append(". But are: ");
		testResults.append(coursePrivateStatePrerequisites);
		testResults.append(".\n");
	}


	private void runDepartemtTests() {
		// check all courses in expected department were really created
		for(DepartmentExpectedOutput departmetExpected : departments.values()) {
			for (String courseExpected : departmetExpected.getCourses().keySet()) {
				if  (coursesPrivateStates.get(courseExpected) == null) {
					testResults.append("####GENERAL-TESTS-ERROR#####\nprerequisites for course ");
					testResults.append(courseExpected);
					testResults.append(" should have been created.\n");
				}
			}
		}

	}


	private void runStudentTests() {
		//check all student actors appear
		for(String ExpectedStudentName : students.keySet()){
			if (!studentsPrivateStates.containsKey(ExpectedStudentName)) {
				testResults.append("####GENERAL-TESTS-ERROR#####\nstudent ");
				testResults.append(ExpectedStudentName);
				testResults.append(" is missing from your program output!! \n");
			}
		}
	}

}
