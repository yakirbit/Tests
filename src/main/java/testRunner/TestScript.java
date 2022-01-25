package testRunner;

import bgu.atd.a1.sim.Simulator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

public class TestScript {
    public static void main(String[] args) {
        StringBuilder passed = new StringBuilder("Maybe passed tests:");
        StringBuilder failed = new StringBuilder("Definitely failed tests:");
        int num = 1;
        int timout;
        for (int i = 1; i <= 12; i++) {
            System.out.println("#################### TEST " + i + " ####################");
            timout = i == 12 || i == 2 ? 600000 : 120000;

            String pwd = "";
            try {
                pwd = new java.io.File(".").getCanonicalPath();
            } catch (Exception ignored){}
            String[] json = {pwd + "\\jsonFiles\\" + i + ".json"};

            System.out.println("Running the simulator");
            Thread t = new Thread(() -> Simulator.main(json));

            String[] test_args = {pwd + "\\jsonFiles\\" + i + ".json", pwd + "\\result.ser", Integer.toString(i)};

            t.start();
            try {
                t.join(timout);
                if (t.isAlive()) {
                    System.out.println("Test " + i + " timed out");
                    t.interrupt();
                    throw new TimeoutException("Timed out after waiting " + timout / 60000  + " minutes for result");
                }

                System.out.println("Running the tester");
                Tester.main(test_args);
                passed.append(" ").append(i);

            } catch (Exception e) {
                System.out.println("Test " + i + " was unsuccessful");
                e.printStackTrace();
                System.out.println(e);
                failed.append(" ").append(i);

                Path file = Paths.get("test_result" + i + ".txt");
                String msg = "Test failed because of an exception:\n " + e;
                try {
                    Files.write(file, msg.getBytes());
                } catch (Exception ignored) {}
            }
        }
        System.out.println(passed);
        System.out.println(failed);
    }
}
