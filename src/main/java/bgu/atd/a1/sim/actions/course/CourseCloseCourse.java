package bgu.atd.a1.sim.actions.course;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.actions.student.StudentUnregister;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseCloseCourse extends Action<Boolean> {
    private String course;

    public CourseCloseCourse(String course) {
        this.course = course;
    }

    @Override
    protected void start() {
        CoursePrivateState courseState = ((CoursePrivateState) getActorState());
        List<Action<Boolean>> actions = new ArrayList<>();
        HashMap<String, Action<Boolean>> studentActions = new HashMap<>();
        for (String student : courseState.getRegStudents()) {
            Action<Boolean> studentUnregister = new StudentUnregister(course);
            actions.add(studentUnregister);
            studentActions.putIfAbsent(student, studentUnregister);
        }

        then(actions, () -> {
            for (Action<Boolean> action : actions) {
                if (!action.getResult().get()) {
                    complete(false);
                    return;
                }
            }
            courseState.setAvailableSpots(-1);
            courseState.setRegistered(0);
            courseState.setRegStudents(null);
            complete(true);
        });

        for(String student: studentActions.keySet()){
            Action<Boolean> studentUnregister = studentActions.get(student);
            sendMessage(studentUnregister, student, new StudentPrivateState());
        }
    }
}
