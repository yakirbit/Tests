package bgu.atd.a1.sim.actions.department;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.student.StudentPassCourses;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DepartmentCheckAdministrativeObligations extends Action<String> {
    private String department;
    private String[] students;
    private String computerId;
    private String[] conditions;
    private CountDownLatch lock;

    public DepartmentCheckAdministrativeObligations(String department, String[] students, String computer, String[] conditions, CountDownLatch lock) {
        this.department = department;
        this.students = students;
        this.computerId = computer;
        this.conditions = conditions;
        this.lock = lock;
    }

    @Override
    protected void start() {
        List<Action<Boolean>> actions = new ArrayList<>();
        for (String student : students) {
            Action<Boolean> confAction = new StudentPassCourses(student, conditions, computerId);
            actions.add(confAction);
        }

        then(actions, () -> {
            int i = 0;
            for (Action<Boolean> action : actions) {
                Boolean confirmationResult = action.getResult().get();
                if (confirmationResult == false) {
                    complete("failed");
                    getActorState().addRecord("Administrative Check");
                    System.out.println(String.format("CheckAdministrativeObligations failed: department %s, student %s", department, students[i]));
                    lock.countDown();
                    return;
                }
                i = i + 1;
            }
            complete("success");
            getActorState().addRecord("Administrative Check");
            System.out.println(String.format("CheckAdministrativeObligations success: department %s, students %s,", department, Arrays.toString(students)));
            lock.countDown();
        });

        int i = 0;
        for (Action action : actions) {
            sendMessage(action, students[i], new StudentPrivateState());
            i = i + 1;
        }
    }
}