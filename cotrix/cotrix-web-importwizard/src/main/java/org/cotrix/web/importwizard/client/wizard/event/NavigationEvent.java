package org.cotrix.web.importwizard.client.wizard.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class NavigationEvent extends GwtEvent<NavigationEvent.NavigationHandler> {
	
	public static final NavigationEvent FORWARD = new NavigationEvent(NavigationType.FORWARD);
	public static final NavigationEvent BACKWARD = new NavigationEvent(NavigationType.BACKWARD);

	public static Type<NavigationHandler> TYPE = new Type<NavigationHandler>();

	public interface NavigationHandler extends EventHandler {
		void onNavigation(NavigationEvent event);
	}

	public interface HasNavigationHandlers extends HasHandlers {
		HandlerRegistration addNavigationHandler(NavigationHandler handler);
	}
	
	public enum NavigationType {BACKWARD, FORWARD};

	protected NavigationType navigationType;
	
	public NavigationEvent(NavigationType navigationType) {
		this.navigationType = navigationType;
	}

	/**
	 * @return the navigationType
	 */
	public NavigationType getNavigationType() {
		return navigationType;
	}

	@Override
	protected void dispatch(NavigationHandler handler) {
		handler.onNavigation(this);
	}

	@Override
	public Type<NavigationHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NavigationHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, NavigationType navigationType) {
		source.fireEvent(new NavigationEvent(navigationType));
	}
}
