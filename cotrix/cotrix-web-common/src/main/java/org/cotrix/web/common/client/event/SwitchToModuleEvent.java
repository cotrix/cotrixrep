package org.cotrix.web.common.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.common.client.CotrixModule;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SwitchToModuleEvent extends GwtEvent<SwitchToModuleEvent.SwitchToModuleHandler> {

	public static Type<SwitchToModuleHandler> TYPE = new Type<SwitchToModuleHandler>();
	private CotrixModule module;

	public interface SwitchToModuleHandler extends EventHandler {
		void onSwitchToModule(SwitchToModuleEvent event);
	}

	public SwitchToModuleEvent(CotrixModule module) {
		this.module = module;
	}

	public CotrixModule getModule() {
		return module;
	}

	@Override
	protected void dispatch(SwitchToModuleHandler handler) {
		handler.onSwitchToModule(this);
	}

	@Override
	public Type<SwitchToModuleHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SwitchToModuleHandler> getType() {
		return TYPE;
	}
}
