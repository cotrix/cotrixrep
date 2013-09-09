package org.cotrix.web.importwizard.client.step;

import org.cotrix.web.importwizard.client.Presenter;

public interface WizardStep extends Presenter {

	public abstract String getId();

	/**
	 * Notify the step controller that the user want leave it.
	 * @return
	 */
	public abstract boolean leave();

}