package bgu.atd.a1.sim.actions.course;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.student.StudentContainCourses;
import bgu.atd.a1.sim.actions.student.StudentUpdateCourseGrade;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CourseParticipatingInCourse extends Action<Boolean> {
    private String student;
    private String course;
    private Integer[] grade;
    private CountDownLatch lock;

    public CourseParticipatingInCourse(String student, String course, Integer[] grade, CountDownLatch lock) {
        this.student = student;
        this.course = course;
        this.grade = grade;
        this.lock = lock;
    }

    @Override
    protected void start() {
        CoursePrivateState courseState = ((CoursePrivateState) getActorState());
        List<String> courses = courseState.getPrequisites();
        Action<Boolean> studentContainCourses = new StudentContainCourses(courses);
        then(Arrays.asList(studentContainCourses), () -> {
            if (studentContainCourses.getResult().get() && courseState.getAvailableSpots() > 0) {
                Action<Boolean> studentUpdateCourseGrade = new StudentUpdateCourseGrade(course, grade);
                then(Arrays.asList(studentUpdateCourseGrade), () -> {
                    if (studentUpdateCourseGrade.getResult().get()) {
                        courseState.setAvailableSpots(courseState.getAvailableSpots() - 1);
                        courseState.setRegistered(courseState.getRegistered() + 1);
                        courseState.getRegStudents().add(student);
                        complete(true);
                        getActorState().addRecord("Participate In Course");
                        System.out.println(String.format("ParticipatingInCourse success: student %s, course %s", student, course));
                    } else {
                        complete(false);
                        getActorState().addRecord("Participate In Course");
                        System.out.println(String.format("ParticipatingInCourse failed: student %s, course %s", student, course));
                    }
                    if (lock != null) {
                        lock.countDown();
                    }
                });
                sendMessage(studentUpdateCourseGrade, student, new StudentPrivateState());
            } else {
                complete(false);
                getActorState().addRecord("Participate In Course");
                System.out.println(String.format("ParticipatingInCourse failed: student %s, course %s", student, course));
                if (lock != null) {
                    lock.countDown();
                }
            }
        });
        sendMessage(studentContainCourses, student, new StudentPrivateState());
    }
}
