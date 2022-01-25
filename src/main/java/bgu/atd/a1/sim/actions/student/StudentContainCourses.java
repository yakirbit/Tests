package bgu.atd.a1.sim.actions.student;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.List;
import java.util.Set;

public class StudentContainCourses extends Action<Boolean> {
    private List<String> courses;

    public StudentContainCourses(List<String> courses) {
        this.courses = courses;
    }

    @Override
    protected void start() {
        StudentPrivateState studentState = (StudentPrivateState) getActorState();
        if (studentState.getGrades().keySet().containsAll(courses)) {
            complete(true);
//            System.out.println(String.format("PreRequisites failed: student %s, course %s", student, this.course));
        } else {
            complete(false);
//            System.out.println(String.format("PreRequisites failed: student %s, course %s", student, this.course));
        }
    }
}