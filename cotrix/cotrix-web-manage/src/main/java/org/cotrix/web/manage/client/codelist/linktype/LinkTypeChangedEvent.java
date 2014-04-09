package org.cotrix.web.manage.client.codelist.linktype;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.common.shared.codelist.link.UILinkType;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypeChangedEvent extends GwtEvent<LinkTypeChangedEvent.LinkTypeChangedHandler> {

	public static Type<LinkTypeChangedHandler> TYPE = new Type<LinkTypeChangedHandler>();
	
	private UILinkType linktype;

	public interface LinkTypeChangedHandler extends EventHandler {
		void onLinkTypeChanged(LinkTypeChangedEvent event);
	}

	public interface HasLinkTypeChangedHandlers extends HasHandlers {
		HandlerRegistration addLinkTypeChangedHandler(
				LinkTypeChangedHandler handler);
	}

	public LinkTypeChangedEvent(UILinkType linktype) {
		this.linktype = linktype;
	}

	public UILinkType getLinkType() {
		return linktype;
	}

	@Override
	protected void dispatch(LinkTypeChangedHandler handler) {
		handler.onLinkTypeChanged(this);
	}

	@Override
	public Type<LinkTypeChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LinkTypeChangedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, UILinkType linktype) {
		source.fireEvent(new LinkTypeChangedEvent(linktype));
	}
}
