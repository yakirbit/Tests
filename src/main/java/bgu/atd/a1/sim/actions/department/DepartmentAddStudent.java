package bgu.atd.a1.sim.actions.department;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.student.StudentAddStudent;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DepartmentAddStudent extends Action<String> {
    private String department;
    private String student;
    private CountDownLatch lock;

    public DepartmentAddStudent(String department, String student, CountDownLatch lock) {
        this.department = department;
        this.student = student;
        this.lock = lock;
    }

    @Override
    protected void start() {
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> confAction = new StudentAddStudent();
        actions.add(confAction);
        then(actions, () -> {
            Boolean confirmationResult = actions.get(0).getResult().get();
            if (confirmationResult == true) {
                ((DepartmentPrivateState) getActorState()).addStudent(student);
                complete("success");
                getActorState().addRecord("Add Student");
                System.out.println(String.format("AddStudent success: department %s, student %s", department, student));
            } else {
                complete("failed");
                getActorState().addRecord("Add Student");
                System.out.println(String.format("AddStudent failed: department %s, student %s", department, student));
            }
            lock.countDown();
        });
        sendMessage(confAction, student, new StudentPrivateState());
    }
}