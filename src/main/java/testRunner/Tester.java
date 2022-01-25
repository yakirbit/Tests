package testRunner;

import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.*;
import sequencialRunner.ComputerExpectedOutput;
import sequencialRunner.CourseExpectedOutput;
import sequencialRunner.DepartmentExpectedOutput;
import sequencialRunner.SequencialOutputCreator;
import sequencialRunner.StudentExpectedOutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/**
 * A class describing the tester of the simulator for part 2 of the assignment.
 * We will compile and run the student's code in the shell script
 * Then for each project we will copy his bgu class's folder into our target/classes folder.
 * Then we will compile and run our tester.
 */
public class Tester {


    private static HashMap<String, DepartmentExpectedOutput> departments;
    private static HashMap<String, StudentExpectedOutput> students;
    private static HashMap<String, CourseExpectedOutput> courses;
    private static HashMap<String, ComputerExpectedOutput> computers;
    private static HashMap<String, PrivateState> departmentsPrivateStates;
    private static HashMap<String, PrivateState> studentsPrivateStates;
    private static HashMap<String, PrivateState> coursesPrivateStates;
    private static String testResults;

    public static void main(String[] args) throws IOException {
        testResults = "";
        ParseJsonInput(args[0]); // send the json input file
        reconstructPrivateStates(ExtractProgramOutput(args[1], args[2])); // send the student's serializable program output
        if (testResults.isEmpty())// if we didn't get exceptions regarding the student's serializable output file.
            testResults = runTests(args[2]).toString();//send the test number.
        outputTestResults(testResults, args[2]);
    }

    private static StringBuilder runTests(String specificTestNum) {
        GeneralTests generalTests = new GeneralTests(departments, students, courses, computers, departmentsPrivateStates, studentsPrivateStates, coursesPrivateStates);
        StringBuilder testResults = generalTests.runTests();
        SpecificTests specificTests = new SpecificTests(departments, students, courses, computers, departmentsPrivateStates, studentsPrivateStates, coursesPrivateStates);
        testResults.append(specificTests.runTests(specificTestNum)); //send the test number.
        return testResults;
    }

    private static void reconstructPrivateStates(HashMap<String, PrivateState> extractedProgramOutput) {
        departmentsPrivateStates = new HashMap<String, PrivateState>();
        studentsPrivateStates = new HashMap<String, PrivateState>();
        coursesPrivateStates = new HashMap<String, PrivateState>();
        for (Entry<String, PrivateState> state : extractedProgramOutput.entrySet()) {
            if (state.getValue() instanceof DepartmentPrivateState)
                departmentsPrivateStates.put(state.getKey(), state.getValue());
            else if (state.getValue() instanceof StudentPrivateState)
                studentsPrivateStates.put(state.getKey(), state.getValue());
            else if (state.getValue() instanceof CoursePrivateState)
                coursesPrivateStates.put(state.getKey(), state.getValue());
        }
    }


    private static void outputTestResults(String testResults, String testNum) throws IOException {
        Path file = Paths.get("test_result" + testNum + ".txt");
        byte[] strToBytes = testResults.getBytes();
        Files.write(file, strToBytes);
    }


    private static void ParseJsonInput(String jsonFilename) {
        SequencialOutputCreator sequencialOutput = new SequencialOutputCreator();
        sequencialOutput.ParseJsonInput(jsonFilename);
        departments = sequencialOutput.getDepartments();
        students = sequencialOutput.getStudents();
        courses = sequencialOutput.getCourses();
        computers = sequencialOutput.getComputers();

    }


    private static HashMap<String, PrivateState> ExtractProgramOutput(String serializedOutputFilename, String testNum) {
        //if (new File("serializedOutputFilename").exists()) {
        try {
            return deserealize(serializedOutputFilename);
        } catch (IOException i) {
            testResults = "################  Could not open the serialized output file for test " + testNum + "!!";
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            testResults = "################  Could not open the serialized output file for test " + testNum + "!!";
            c.printStackTrace();
        }
        //}else {
        //	testResults="###############  No serialized output file found for test "+testNum+"!!";
        //}
        return null;

    }


    @SuppressWarnings("unchecked")
    private static HashMap<String, PrivateState> deserealize(String serializedOutputFilename)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(serializedOutputFilename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        HashMap<String, PrivateState> programOutputPrivateStates = (HashMap<String, PrivateState>) in.readObject();
        in.close();
        fileIn.close();
        return programOutputPrivateStates;
    }


}
