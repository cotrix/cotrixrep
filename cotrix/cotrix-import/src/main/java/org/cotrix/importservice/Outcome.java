package org.cotrix.importservice;

import org.cotrix.domain.trait.Named;

/**
 * The outcome of an import task.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the object to import
 */
public class Outcome<T extends Named> {

	private final T result;
	private final Report report;
	
	/**
	 * Creates an instance with the result of the import task.
	 * @param result the results
	 */
	public Outcome(T result) {
		this.result=result;
		this.report= Report.report();
		report.close();
	}
	
	/**
	 * Return the report of the import task.
	 * @return the report
	 */
	public String report() {
		return report.toString();
	}
	
	/**
	 * Returns the result of the import task.
	 * @return the result
	 * @throws ImportFailureException if the import task failed (the problems can be found in the report).
	 */
	public T result() throws ImportFailureException {
		
		if (report.isFailure())
			throw new ImportFailureException("failed to import "+result.name());
		else
			return result;
	}
}
