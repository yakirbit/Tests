package bgu.atd.a1.sim;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JsonParser {

    public class ComputerObject {
        @SerializedName("Type") String type;
        @SerializedName("Sig Success") long sig_success;
        @SerializedName("Sig Fail") long sig_fail;
    }

    public class ActionObject {
        @SerializedName("Action") String action;
        @SerializedName("Department") String department;
        @SerializedName("Course") String course;
        @SerializedName("Space") int space;
        @SerializedName("Prerequisites") List<String> prerequisites;
        @SerializedName("Student") String student;
        @SerializedName("Grade") Integer[] grade;
        @SerializedName("Students") String[] students;
        @SerializedName("Computer") String computer;
        @SerializedName("Conditions") String[] conditions;
        @SerializedName("Number") int number;
        @SerializedName("Preferences") String[] preferences;
    }

    @SerializedName("threads") int threads;
    @SerializedName("Computers") ComputerObject[] computers;
    @SerializedName("Phase 1") ActionObject[] phase_1;
    @SerializedName("Phase 2") ActionObject[] phase_2;
    @SerializedName("Phase 3") ActionObject[] phase_3;
}
