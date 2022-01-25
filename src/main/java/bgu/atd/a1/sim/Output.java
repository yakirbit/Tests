package bgu.atd.a1.sim;

import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Output {
    class Department {
        @SerializedName("Department")
        private String department;
        private List<String> actions;
        private List<String> courseList;
        private List<String> studentList;

        public Department(String department, List<String> actions, List<String> courseList, List<String> studentList) {
            this.department = department;
            this.actions = actions;
            this.courseList = courseList;
            this.studentList = studentList;
        }
    }

    class Course {
        @SerializedName("Course")
        private String course;
        @SerializedName("actions")
        private List<String> actions;
        @SerializedName("availableSpots")
        private String availableSpots;
        @SerializedName("registered")
        private String registered;
        @SerializedName("regStudents")
        private List<String> regStudents;
        @SerializedName("prequisites")
        private List<String> prequisites;

        public Course(String course, List<String> actions, String availableSpots, String registered, List<String> regStudents, List<String> prequisites) {
            this.course = course;
            this.actions = actions;
            this.availableSpots = availableSpots;
            this.registered = registered;
            this.regStudents = regStudents;
            this.prequisites = prequisites;
        }
    }

    class Student {
        @SerializedName("Student")
        private String student;
        @SerializedName("actions")
        private List<String> actions;
        @SerializedName("grades")
        private List<String> grades;
        @SerializedName("signature")
        private String signature;

        public Student(String student, List<String> actions, List<String> grades, String signature) {
            this.student = student;
            this.actions = actions;
            this.grades = grades;
            this.signature = signature;
        }
    }

    @SerializedName("Departments")
    List<Department> departments = new ArrayList<>();
    @SerializedName("Courses")
    List<Course> courses = new ArrayList<>();
    @SerializedName("Students")
    List<Student> students = new ArrayList<>();

    public Output(HashMap<String, PrivateState> actorsState) {
        for (String key : actorsState.keySet()) {
            PrivateState state = actorsState.get(key);
            if (state instanceof DepartmentPrivateState) {
                DepartmentPrivateState departmentState = (DepartmentPrivateState) actorsState.get(key);
                departments.add(new Department(key, departmentState.getLogger(), departmentState.getCourseList(), departmentState.getStudentList()));
            }
            if (state instanceof CoursePrivateState) {
                CoursePrivateState courseState = (CoursePrivateState) actorsState.get(key);
                courses.add(new Course(key, courseState.getLogger(), Integer.toString(courseState.getAvailableSpots()), Integer.toString(courseState.getRegistered()), courseState.getRegStudents(), courseState.getPrequisites()));
            }
            if (state instanceof StudentPrivateState) {
                StudentPrivateState studentState = (StudentPrivateState) actorsState.get(key);
                students.add(new Student(key, studentState.getLogger(), studentState.getGradesSer(), Long.toString(studentState.getSignature())));
            }
        }
    }
}


