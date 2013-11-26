package org.cotrix.repository;

/**
 * A range of {@link MultiQuery} results.
 *  
 * @author Fabio Simeoni
 *
 */
public class Range {

	public static final Range ALL = new Range(1,Integer.MAX_VALUE);
			
	private final int from;
	private final int to;
	
	/**
	 * Creates an instance with a lower bound and upper bound
	 * @param from the lower bound
	 * @param to the upper bound
	 */
	public Range(int from, int to) {
		this.from=from;
		this.to=to;
	}
	
	/**
	 * Returns the lower bound of this range.
	 * @return the lower bound
	 */
	public int from() {
		return from;
	}
	
	/**
	 * Returns the upper bound of this range.
	 * @return the upper bound
	 */
	public int to() {
		return to;
	}
}
