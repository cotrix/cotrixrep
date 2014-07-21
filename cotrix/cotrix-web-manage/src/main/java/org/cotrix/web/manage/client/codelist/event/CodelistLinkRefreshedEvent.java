package org.cotrix.web.manage.client.codelist.event;

import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 * @param <T>
 */
public class CodelistLinkRefreshedEvent extends GwtEvent<CodelistLinkRefreshedEvent.CodelistLinkRefreshedHandler> {

	public static Type<CodelistLinkRefreshedHandler> TYPE = new Type<CodelistLinkRefreshedHandler>();

	public interface CodelistLinkRefreshedHandler extends EventHandler {
		void onLinkRefreshed(CodelistLinkRefreshedEvent event);
	}

	public interface HasDataEditHandlers<T> extends HasHandlers {
		HandlerRegistration addDataEditHandler(CodelistLinkRefreshedHandler handler);
	}

	private String codelistId;
	private UILinkDefinition linkType;

	public CodelistLinkRefreshedEvent(String codelistId, UILinkDefinition linkType) {
		this.codelistId = codelistId;
		this.linkType = linkType;
	}	

	public String getCodelistId() {
		return codelistId;
	}

	public UILinkDefinition getLinkType() {
		return linkType;
	}


	@Override
	protected void dispatch(CodelistLinkRefreshedHandler handler) {
		handler.onLinkRefreshed(this);
	}

	@Override
	public Type<CodelistLinkRefreshedHandler> getAssociatedType() {
		return TYPE;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodelistLinkRefreshedEvent [linkType=");
		builder.append(linkType);
		builder.append("]");
		return builder.toString();
	}
}

	
