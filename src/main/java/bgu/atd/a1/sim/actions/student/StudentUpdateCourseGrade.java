package bgu.atd.a1.sim.actions.student;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class StudentUpdateCourseGrade extends Action<Boolean> {
    private String course;
    private Integer[] grade;

    public StudentUpdateCourseGrade(String course, Integer[] grade) {
        this.course = course;
        this.grade = grade;
    }

    @Override
    protected void start() {
        StudentPrivateState studentState = (StudentPrivateState) getActorState();
        studentState.addGrade(course, grade[0]);
        complete(true);
    }
}
