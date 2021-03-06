/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step;

import org.cotrix.web.wizard.client.progresstracker.ProgressTracker.ProgressStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum TrackerLabels implements ProgressStep {
	SELECTION("Select"),
	DESTINATION("Direct"),
	TYPE("Format"),
	TARGET("Target"),
	CSVCONFIG("Encode"),
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
