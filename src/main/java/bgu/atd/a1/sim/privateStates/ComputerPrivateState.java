package bgu.atd.a1.sim.privateStates;

import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.Computer;

import java.util.ArrayList;
import java.util.List;

/**
 * this class describe department's private state
 */
public class ComputerPrivateState extends PrivateState {
    private Computer computer;

    /**
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     */
    public ComputerPrivateState() {
        super();
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(String computerType, long failSig, long successSig) {
        this.computer = new Computer(computerType);
        this.computer.setFailSig(failSig);
        this.computer.setSuccessSig(successSig);
    }


}
