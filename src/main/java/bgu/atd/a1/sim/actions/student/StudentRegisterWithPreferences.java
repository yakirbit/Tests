package bgu.atd.a1.sim.actions.student;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.course.CourseParticipatingInCourse;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class StudentRegisterWithPreferences extends Action<Boolean> {
    private String student;
    private String[] preferences;
    private Integer[] grade;
    private CountDownLatch lock;

    public StudentRegisterWithPreferences(String student, String[] preferences, Integer[] grade, CountDownLatch lock) {
        this.student = student;
        this.preferences = preferences;
        this.grade = grade;
        this.lock = lock;
    }

    @Override
    protected void start() {
        if (preferences.length > 0) {
            Integer[] courseGrade = {grade[0]};
            Action<Boolean> participatingInCourse = new CourseParticipatingInCourse(student, preferences[0], courseGrade, null);
            then(Arrays.asList(participatingInCourse), () -> {
                if (participatingInCourse.getResult().get()) {
                    complete(true);
                    lock.countDown();
                } else {
                    String[] newPreferences = Arrays.copyOfRange(preferences, 1, preferences.length);
                    Integer[] newGrade = Arrays.copyOfRange(grade, 1, grade.length);
                    Action<Boolean> studentRegisterWithPreferences = new StudentRegisterWithPreferences(student, newPreferences, newGrade, lock);
                    then(Arrays.asList(studentRegisterWithPreferences), () -> {
                        if (studentRegisterWithPreferences.getResult().get()) {
                            complete(true);
                            getActorState().addRecord("Register With Preferences");
                            System.out.println(String.format("StudentRegisterWithPreferences success: student %s, preferences %s", student, Arrays.toString(preferences)));
                            lock.countDown();
                        } else {
                            getActorState().addRecord("Register With Preferences");
                            System.out.println(String.format("StudentRegisterWithPreferences failed: student %s, preferences %s", student, Arrays.toString(preferences)));
                            complete(false);
                            lock.countDown();
                        }
                    });
                    sendMessage(studentRegisterWithPreferences, student, new StudentPrivateState());
                }
            });
            sendMessage(participatingInCourse, preferences[0], new CoursePrivateState());
        } else {
            complete(false);
            lock.countDown();
        }
    }
}
