package org.cotrix.common;


/**
 * The outcome of a task.
 * 
 * @author Fabio Simeoni
 * 
 */
public class Outcome<T> {

	private final T result;
	private final Report report;

	/**
	 * Creates an instance with the result of the task.
	 * 
	 * @param result the results
	 */
	public Outcome(T result) {
		this.result = result;
		this.report = Report.report();
		report.close();
	}

	/**
	 * Returns a report on the task.
	 * 
	 * @return the report
	 */
	public Report report() {
		return report;
	}

	/**
	 * Returns the task result.
	 * 
	 * @return the result
	 * @throws RuntimeException if the task fails (causes may be found in the report)
	 */
	public T result() {

		if (report.isFailure())
			throw new RuntimeException("failed to publish ");// + result.name());
		else
			return result;
	}

	@Override
	public String toString() {
		return "Outcome [result=" + result + ", report=" + report + "]";
	}
	
}
