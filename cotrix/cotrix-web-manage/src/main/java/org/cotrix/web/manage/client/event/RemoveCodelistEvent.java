package org.cotrix.web.manage.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

public class RemoveCodelistEvent extends
		GwtEvent<RemoveCodelistEvent.RemoveCodelistHandler> {

	public static Type<RemoveCodelistHandler> TYPE = new Type<RemoveCodelistHandler>();
	private String codelistId;

	public interface RemoveCodelistHandler extends EventHandler {
		void onRemoveCodelist(RemoveCodelistEvent event);
	}

	public RemoveCodelistEvent(String codelistId) {
		this.codelistId = codelistId;
	}

	public String getCodelistId() {
		return codelistId;
	}

	@Override
	protected void dispatch(RemoveCodelistHandler handler) {
		handler.onRemoveCodelist(this);
	}

	@Override
	public Type<RemoveCodelistHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RemoveCodelistHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String codelistId) {
		source.fireEvent(new RemoveCodelistEvent(codelistId));
	}
}
