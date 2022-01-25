/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.atd.a1.sim;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.actions.computer.ComputerOpen;
import bgu.atd.a1.sim.actions.course.CourseAddSpace;
import bgu.atd.a1.sim.actions.course.CourseParticipatingInCourse;
import bgu.atd.a1.sim.actions.course.CourseUnregister;
import bgu.atd.a1.sim.actions.department.*;
import bgu.atd.a1.sim.actions.student.StudentRegisterWithPreferences;
import bgu.atd.a1.sim.privateStates.ComputerPrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

    public static ActorThreadPool actorThreadPool;
    public static JsonParser json;

    /**
     * Begin the simulation Should not be called before attachActorThreadPool()
     */
    public static void start() {
        actorThreadPool.start();

        Action[] computers = new Action[json.computers.length];
        CountDownLatch lock2 = new CountDownLatch(computers.length);
        for (int i = 0; i < computers.length; i++) {
            computers[i] = new ComputerOpen(json.computers[i].type, json.computers[i].sig_success, json.computers[i].sig_fail, lock2);
            actorThreadPool.submit(computers[i], json.computers[i].type, new ComputerPrivateState());
        }

        try {
            lock2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Action[][] actions = new Action[3][];
        actions[0] = new Action[json.phase_1.length];
        actions[1] = new Action[json.phase_2.length];
        actions[2] = new Action[json.phase_3.length];

        for (int phase = 1; phase <= 3; phase++) {
            JsonParser.ActionObject[] actionObjects = null;
            if (phase == 1) {
                actionObjects = json.phase_1;
            }
            if (phase == 2) {
                actionObjects = json.phase_2;
            }
            if (phase == 3) {
                actionObjects = json.phase_3;
            }

            CountDownLatch lock = new CountDownLatch(actions[phase - 1].length);

            for (int i = 0; i < actions[phase - 1].length; i++) {
                JsonParser.ActionObject action = actionObjects[i];
                if (action.action.equals("Open Course")) {
                    actions[phase - 1][i] = new DepartmentOpenCourse(action.department, action.course, action.space, action.prerequisites, lock);
                    actorThreadPool.submit(actions[phase - 1][i], action.department, new DepartmentPrivateState());
                }

                if (action.action.equals("Add Student")) {
                    actions[phase - 1][i] = new DepartmentAddStudent(action.department, action.student, lock);
                    actorThreadPool.submit(actions[phase - 1][i], action.department, new DepartmentPrivateState());
                }

                if (action.action.equals("Participate In Course")) {
                    actions[phase - 1][i] = new CourseParticipatingInCourse(action.student, action.course, action.grade, lock);
                    actorThreadPool.submit(actions[phase - 1][i], action.course, new CoursePrivateState());
                }

                if (action.action.equals("Unregister")) {
                    actions[phase - 1][i] = new CourseUnregister(action.course, action.student, lock);
                    actorThreadPool.submit(actions[phase - 1][i], action.course, new CoursePrivateState());
                }

                if (action.action.equals("Close Course")) {
                    actions[phase - 1][i] = new DepartmentCloseCourse(action.department, action.course, lock);
                    actorThreadPool.submit(actions[phase - 1][i], action.department, new DepartmentPrivateState());
                }

                if (action.action.equals("Add Spaces")) {
                    actions[phase - 1][i] = new CourseAddSpace(action.course, action.number, lock);
                    actorThreadPool.submit(actions[phase - 1][i], action.course, new DepartmentPrivateState());
                }

                if (action.action.equals("Administrative Check")) {
                    actions[phase - 1][i] = new DepartmentCheckAdministrativeObligations(action.department, action.students, action.computer, action.conditions, lock);
                    actorThreadPool.submit(actions[phase - 1][i], action.department, new DepartmentPrivateState());
                }

                if (action.action.equals("Register With Preferences")) {
                    actions[phase - 1][i] = new StudentRegisterWithPreferences(action.student, action.preferences, action.grade, lock);
                    actorThreadPool.submit(actions[phase - 1][i], action.student, new StudentPrivateState());
                }
            }

            try {
                lock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(String.format("Phase %d finished.\n", phase));
        }
    }

    /**
     * attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
     *
     * @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
     */
    public static void attachActorThreadPool(ActorThreadPool myActorThreadPool) {
        actorThreadPool = myActorThreadPool;
    }

    /**
     * shut down the simulation
     * returns list of private states
     */
    public static HashMap<String, PrivateState> end() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter outputFile = new FileWriter("result.ser");
            Output outputObject = new Output((HashMap<String, PrivateState>) actorThreadPool.getActors());
            gson.toJson(outputObject, outputFile);
            outputFile.close(); // close file after finish writing
            actorThreadPool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (HashMap<String, PrivateState>) actorThreadPool.getActors();
    }


    public static void main(String[] args) {
        Gson gson = new Gson();
        try {
            json = gson.fromJson(new FileReader(args[0]), JsonParser.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        attachActorThreadPool(new ActorThreadPool(json.threads));
        System.out.println("Start");
        start();
        end();
        System.out.println("Finish");
    }
}
