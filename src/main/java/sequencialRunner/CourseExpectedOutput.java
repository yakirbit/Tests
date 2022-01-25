package sequencialRunner;
import java.util.HashMap;
import java.util.List;

public class CourseExpectedOutput{
	private int initialAmountOfPlaces;
	private int numberOfRegestered;
	private int availableSpots;
	private HashMap<String,Integer> studentsRegestered;
	private List<String> prerequisites;
	
	/**
	 * @param availableSpots
	 * @param numberOfRegestered
	 * @param studentsRegestered
	 * @param prerequisites
	 */
	public CourseExpectedOutput(int availableSpots, int numberOfRegestered, HashMap<String, Integer> studentsRegestered, List<String> prerequisites) {
		initialAmountOfPlaces = availableSpots;
		this.availableSpots = availableSpots;
		this.numberOfRegestered = numberOfRegestered;
		this.studentsRegestered = studentsRegestered;
		this.prerequisites = prerequisites;
	}
	

	/**
	 * @return the initialAmountOfPlaces
	 */
	public int getInitialAmountOfPlaces() {
		return initialAmountOfPlaces;
	}
	/**
	 * @return the availableSpots
	 */
	public int getAvailableSpots() {
		return availableSpots;
	}
	/**
	 * @param availableSpots the availableSpots to set
	 */
	public void setAvailableSpots(int availableSpots) {
		this.availableSpots = availableSpots;
	}
	/**
	 * @return the numberOfRegestered
	 */
	public int getNumberOfRegestered() {
		return numberOfRegestered;
	}
	/**
	 * @param numberOfRegestered the numberOfRegestered to set
	 */
	public void setNumberOfRegestered(int numberOfRegestered) {
		this.numberOfRegestered = numberOfRegestered;
	}
	/**
	 * @return the studentsRegestered
	 */
	public HashMap<String, Integer> getStudentsRegestered() {
		return studentsRegestered;
	}
	/**
	 * @param studentsRegestered the studentsRegestered to set
	 */
	public void addStudentToStudentsRegestered(String student) {
		if (!studentsRegestered.containsKey(student)) {
			studentsRegestered.put(student, 0);
			numberOfRegestered++;
			availableSpots--;
		}
	}
	/**
	 * @param studentsRegestered the studentsRegestered to set
	 */
	public void RemoveStudentFromStudentsRegestered(String student) {
		if (studentsRegestered.containsKey(student)) {
			studentsRegestered.remove(student);
			numberOfRegestered--;
			availableSpots++;
		}
	}
	/**
	 * @param 
	 */
	public void RemoveAllStudentsRegestered() {
		while (!studentsRegestered.isEmpty())
			studentsRegestered = new HashMap<String,Integer>();
			availableSpots=availableSpots+numberOfRegestered;
			numberOfRegestered=0;
	}
	/**
	 * @return the prerequisites
	 */
	public List<String> getPrerequisites() {
		return prerequisites;
	}
	/**
	 * @param prerequisites the prerequisites to set
	 */
	public void setPrerequisites(List<String> prerequisites) {
		this.prerequisites = prerequisites;
	}
}


