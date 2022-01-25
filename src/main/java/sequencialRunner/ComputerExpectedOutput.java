package sequencialRunner;

public class ComputerExpectedOutput{

	private long sigSuccess;
	private long sigFail;
	
	/**
	 * @param sigSuccess
	 * @param sigFail
	 */
	public ComputerExpectedOutput(long sigSuccess, long sigFail) {
		this.sigSuccess = sigSuccess;
		this.sigFail = sigFail;
	}
	public ComputerExpectedOutput() {
		this.sigSuccess = -1;
		this.sigFail = -1;
	}

	/**
	 * @return the sigSuccess
	 */
	public long getSigSuccess() {
		return sigSuccess;
	}

	/**
	 * @param sigSuccess the sigSuccess to set
	 */
	public void setSigSuccess(long sigSuccess) {
		this.sigSuccess = sigSuccess;
	}

	/**
	 * @return the sigFail
	 */
	public long getSigFail() {
		return sigFail;
	}

	/**
	 * @param sigFail the sigFail to set
	 */
	public void setSigFail(long sigFail) {
		this.sigFail = sigFail;
	}

}
