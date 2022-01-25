package bgu.atd.a1.sim.actions.department;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.course.CourseCloseCourse;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class DepartmentCloseCourse extends Action<String> {
    private String department;
    private String course;
    private CountDownLatch lock;

    public DepartmentCloseCourse(String department, String course, CountDownLatch lock) {
        this.department = department;
        this.course = course;
        this.lock = lock;
    }

    @Override
    protected void start() {
        DepartmentPrivateState departmentState = (DepartmentPrivateState) getActorState();
        Action<Boolean> courseCloseCourse = new CourseCloseCourse(course);
        then(Arrays.asList(courseCloseCourse), () -> {
            if (courseCloseCourse.getResult().get()) {
                departmentState.removeCourse(course);
                complete("success");
                getActorState().addRecord("Open Course");
                System.out.println(String.format("OpenCourse success: department %s, course %s", department, course));
            } else {
                complete("failed");
                getActorState().addRecord("Open Course");
            }
            lock.countDown();
        });
        sendMessage(courseCloseCourse, course, new CoursePrivateState());
    }
}