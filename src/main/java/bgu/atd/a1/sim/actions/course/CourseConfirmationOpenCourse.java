package bgu.atd.a1.sim.actions.course;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CourseConfirmationOpenCourse extends Action<Boolean> {
    private String department;
    private String course;
    private int space;
    private List<String> preRequisites;

    public CourseConfirmationOpenCourse(String department, String course, int space, List<String> preRequisites) {
        this.department = department;
        this.course = course;
        this.space = space;
        this.preRequisites = preRequisites;
    }

    @Override
    protected void start() {
        CoursePrivateState state = ((CoursePrivateState) getActorState());
        state.setAvailableSpots(space);
        state.setPrequisites(preRequisites);
        state.setRegistered(0);
        state.setRegStudents(new ArrayList<>());
        complete(true);
    }
}