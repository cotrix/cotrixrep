/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step;

import org.cotrix.web.share.client.wizard.progresstracker.ProgressTracker.ProgressStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum TrackerLabels implements ProgressStep {
	SELECTION("Selection"),
	DESTINATION("Destination"),
	TYPE("Type"),
	LOCATE("Locate"),
	CSVCONFIG("CSV config"),
	CUSTOMIZE("Customize"),
	SUMMARY("Summary"),
	DONE("Done"),
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
