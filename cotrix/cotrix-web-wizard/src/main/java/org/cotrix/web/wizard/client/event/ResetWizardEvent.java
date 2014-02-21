package org.cotrix.web.wizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ResetWizardEvent extends GwtEvent<ResetWizardEvent.ResetWizardHandler> {

	public static Type<ResetWizardHandler> TYPE = new Type<ResetWizardHandler>();

	public interface ResetWizardHandler extends EventHandler {
		void onResetWizard(ResetWizardEvent event);
	}

	public ResetWizardEvent() {
	}

	@Override
	protected void dispatch(ResetWizardHandler handler) {
		handler.onResetWizard(this);
	}

	@Override
	public Type<ResetWizardHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ResetWizardHandler> getType() {
		return TYPE;
	}
}
