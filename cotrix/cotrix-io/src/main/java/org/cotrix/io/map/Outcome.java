package org.cotrix.io.map;

import org.cotrix.domain.Codelist;

/**
 * The outcome of a mapping a codelist onto a domain object.
 * 
 * @author Fabio Simeoni
 * 
 */
public class Outcome {

	private final Codelist result;
	private final Report report;

	/**
	 * Creates an instance with the domain object.
	 * 
	 * @param result the results
	 */
	public Outcome(Codelist result) {
		this.result = result;
		this.report = Report.report();
		report.close();
	}

	/**
	 * Returns a report on the mapping.
	 * 
	 * @return the report
	 */
	public String report() {
		return report.toString();
	}

	/**
	 * Returns the domain object.
	 * 
	 * @return the result
	 * @throws RuntimeException if the import task fails (causes may be found in the report).
	 */
	public Codelist result() {

		if (report.isFailure())
			throw new RuntimeException("failed to import " + result.name());
		else
			return result;
	}

	@Override
	public String toString() {
		return "Outcome [result=" + result + ", report=" + report + "]";
	}
	
}
