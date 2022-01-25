package bgu.atd.a1.sim.actions.course;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.List;
import java.util.Set;

public class CourseGetPrerequisites extends Action<List<String>> {
    @Override
    protected void start() {
        List<String> prerequisites = ((CoursePrivateState) getActorState()).getPrequisites();
        complete(prerequisites);
    }
}