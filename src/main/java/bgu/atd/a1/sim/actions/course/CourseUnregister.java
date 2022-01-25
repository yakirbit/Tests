package bgu.atd.a1.sim.actions.course;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.student.StudentUnregister;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class CourseUnregister extends Action<Boolean> {
    private String course;
    private String student;
    private CountDownLatch lock;

    public CourseUnregister(String course, String student, CountDownLatch lock) {
        this.course = course;
        this.student = student;
        this.lock = lock;
    }

    @Override
    protected void start() {
        CoursePrivateState courseState = ((CoursePrivateState) getActorState());
        Action<Boolean> studentUnregister = new StudentUnregister(course);
        then(Arrays.asList(studentUnregister), () -> {
            if (studentUnregister.getResult().get()) {
                courseState.setAvailableSpots(courseState.getAvailableSpots() + 1);
                courseState.setRegistered(courseState.getRegistered() - 1);
                courseState.getRegStudents().remove(student);
                complete(true);
                getActorState().addRecord("Unregister");
                System.out.println(String.format("UnregisterCourse success: course %s, student %s", course, student));
            } else {
                complete(false);
                getActorState().addRecord("Unregister");
                System.out.println(String.format("UnregisterCourse failed: course %s, student %s", course, student));
            }
            lock.countDown();
        });
        sendMessage(studentUnregister, student, new StudentPrivateState());
    }
}