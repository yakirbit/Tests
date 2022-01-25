package bgu.atd.a1.sim.actions.computer;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.ComputerPrivateState;

import java.util.List;
import java.util.Map;

public class ComputerCheckAndSign extends Action<Long> {
    private List<String> courses;
    private Map<String, Integer> coursesGrades;

    public ComputerCheckAndSign(List<String> courses, Map<String, Integer> coursesGrades) {
        this.courses = courses;
        this.coursesGrades = coursesGrades;
    }

    @Override
    protected void start() {
        ComputerPrivateState state = ((ComputerPrivateState) getActorState());
        long result = state.getComputer().checkAndSign(courses, coursesGrades);
        complete(result);
    }
}