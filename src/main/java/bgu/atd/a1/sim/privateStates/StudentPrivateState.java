package bgu.atd.a1.sim.privateStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import bgu.atd.a1.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState {
    private HashMap<String, Integer> grades = new HashMap<>();
    private long signature;

    /**
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     */
    public StudentPrivateState() {
    }

    public HashMap<String, Integer> getGrades() {
        return grades;
    }

    public List<String> getGradesSer() {
        List<String> l = new ArrayList<>();
        for (String key : grades.keySet()) {
            l.add(String.format("(%s, %s)", key, grades.get(key)));
        }
        return l;
    }

    public void addGrade(String course, Integer grade) {
        grades.putIfAbsent(course, grade);
    }

    public void removeCourse(String course) {
        grades.remove(course);
    }

    public long getSignature() {
        return signature;
    }

    public void setSignature(long signature) {
        this.signature = signature;
    }

    public void setGrades(HashMap<String, Integer> grades) {
        this.grades = grades;
    }
}
