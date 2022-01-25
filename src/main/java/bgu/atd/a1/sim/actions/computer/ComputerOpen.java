package bgu.atd.a1.sim.actions.computer;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.ComputerPrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ComputerOpen extends Action<Boolean> {
    private String computerType;
    private long failSig;
    private long successSig;
    private CountDownLatch lock2;

    public ComputerOpen(String computerType, long failSig, long successSig, CountDownLatch lock2) {
        this.computerType = computerType;
        this.failSig = failSig;
        this.successSig = successSig;
        this.lock2 = lock2;
    }

    @Override
    protected void start() {
        ComputerPrivateState state = ((ComputerPrivateState) getActorState());
        state.setComputer(computerType, failSig, successSig);
        complete(true);
        lock2.countDown();
    }
}