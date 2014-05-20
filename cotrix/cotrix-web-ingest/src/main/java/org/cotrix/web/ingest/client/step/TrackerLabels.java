/**
 * 
 */
package org.cotrix.web.ingest.client.step;

import org.cotrix.web.wizard.client.progresstracker.ProgressTracker.ProgressStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum TrackerLabels implements ProgressStep {
	CUSTOMIZE("Customize"),
	PREVIEW("Preview"),
	DONE("Done"),
	ACQUIRE("Acquire"),
	LOCATE("Locate"),
	SUMMARY("Summary")
	;

	protected String label;

	/**
	 * @param label
	 */
	private TrackerLabels(String label) {
		this.label = label;
	}

	@Override
	public String getId() {
		return toString();
	}

	@Override
	public String getLabel() {
		return label;
	}

}
