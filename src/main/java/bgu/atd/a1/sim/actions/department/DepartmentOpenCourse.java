package bgu.atd.a1.sim.actions.department;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.course.CourseConfirmationOpenCourse;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DepartmentOpenCourse extends Action<String> {
    private String department;
    private String course;
    private int space;
    private List<String> preRequisites;
    private CountDownLatch lock;

    public DepartmentOpenCourse(String department, String course, int space, List<String> preRequisites, CountDownLatch lock) {
        this.department = department;
        this.course = course;
        this.space = space;
        this.preRequisites = preRequisites;
        this.lock = lock;
    }

    @Override
    protected void start() {
        DepartmentPrivateState departmentState = (DepartmentPrivateState) getActorState();
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> confAction = new CourseConfirmationOpenCourse(department, course, space, preRequisites);
        actions.add(confAction);
        then(actions, () -> {
            Boolean confirmationResult = actions.get(0).getResult().get();
            if (confirmationResult == true) {
                departmentState.addCourse(course);
                getActorState().addRecord("Open Course");
                complete("success");
                System.out.println(String.format("OpenCourse success: department %s, course %s", department, course));
            } else {
                complete("failed");
                getActorState().addRecord("Open Course");
                System.out.println(String.format("OpenCourse failed: department %s, course %s", department, course));
            }
            lock.countDown();
        });
        sendMessage(confAction, course, new CoursePrivateState());
    }
}