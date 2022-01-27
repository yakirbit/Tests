package bgu.atd.a1.sim.actions.student;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class StudentUnregister extends Action<Boolean> {
    private String course;

    public StudentUnregister(String course) {
        this.course = course;
    }

    @Override
    protected void start() {
        // not main action hence we don't do countdown to the latch
        StudentPrivateState studentState = ((StudentPrivateState) getActorState());
        if (studentState.getGrades().keySet().contains(course)) {
            studentState.removeCourse(course);
            complete(true);
        } else {
            complete(false);
        }
    }
}