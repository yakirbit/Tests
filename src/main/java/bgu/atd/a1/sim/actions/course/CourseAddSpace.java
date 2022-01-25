package bgu.atd.a1.sim.actions.course;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.course.CourseCloseCourse;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class CourseAddSpace extends Action<Boolean> {
    private String course;
    private Integer space;
    private CountDownLatch lock;

    public CourseAddSpace(String course, Integer space, CountDownLatch lock) {
        this.course = course;
        this.space = space;
        this.lock = lock;
    }

    @Override
    protected void start() {
        CoursePrivateState courseState = (CoursePrivateState) getActorState();
        courseState.setAvailableSpots(courseState.getAvailableSpots() + space);
        complete(true);
        lock.countDown();
    }
}