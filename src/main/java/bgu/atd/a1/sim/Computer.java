package bgu.atd.a1.sim;

import java.util.List;
import java.util.Map;

public class Computer {

    String computerType;
    long failSig;
    long successSig;

    public Computer(String computerType) {
        this.computerType = computerType;
    }

    /**
     * this method checks if the courses' grades fulfill the conditions
     *
     * @param courses       courses that should be pass
     * @param coursesGrades courses' grade
     * @return a signature if couersesGrades grades meet the conditions
     */
    public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades) {
        for (String course : courses) {
            if (!coursesGrades.containsKey(course) || coursesGrades.get(course) < 56) {
                return failSig;
            }
        }
        return successSig;
    }

    public String getComputerType() {
        return computerType;
    }

    public long getFailSig() {
        return failSig;
    }

    public long getSuccessSig() {
        return successSig;
    }

    public void setComputerType(String computerType) {
        this.computerType = computerType;
    }

    public void setFailSig(long failSig) {
        this.failSig = failSig;
    }

    public void setSuccessSig(long successSig) {
        this.successSig = successSig;
    }

}
