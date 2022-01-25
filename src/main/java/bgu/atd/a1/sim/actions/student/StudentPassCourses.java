package bgu.atd.a1.sim.actions.student;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.computer.ComputerCheckAndSign;
import bgu.atd.a1.sim.privateStates.ComputerPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StudentPassCourses extends Action<Boolean> {
    private String student;
    private String[] courses;
    String computerId;

    public StudentPassCourses(String student, String[] courses, String computerId) {
        this.student = student;
        this.courses = courses;
        this.computerId = computerId;
    }

    @Override
    protected void start() {
        HashMap<String, Integer> grades = ((StudentPrivateState) getActorState()).getGrades();
        Action<Long> computerCheckAndSign = new ComputerCheckAndSign(Arrays.asList(courses), grades);
        then(Arrays.asList(computerCheckAndSign), () -> {
            long signature = computerCheckAndSign.getResult().get();
            ((StudentPrivateState) getActorState()).setSignature(signature);
            complete(true);
            System.out.println(String.format("PreRequisitesWithPass success: student %s, courses %s", student, Arrays.toString(courses)));
        });
        sendMessage(computerCheckAndSign, computerId, new ComputerPrivateState());
    }
}