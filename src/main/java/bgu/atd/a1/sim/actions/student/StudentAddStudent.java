package bgu.atd.a1.sim.actions.student;

import bgu.atd.a1.Action;

public class StudentAddStudent extends Action<Boolean> {
    @Override
    protected void start() {
        complete(true);
    }
}