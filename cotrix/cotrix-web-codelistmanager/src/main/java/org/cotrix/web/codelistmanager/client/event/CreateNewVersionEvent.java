package org.cotrix.web.codelistmanager.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CreateNewVersionEvent extends
		GwtEvent<CreateNewVersionEvent.CreateNewVersionHandler> {

	public static Type<CreateNewVersionHandler> TYPE = new Type<CreateNewVersionHandler>();
	
	private String codelistId;
	private String newVersion;

	public interface CreateNewVersionHandler extends EventHandler {
		void onCreateNewVersion(CreateNewVersionEvent event);
	}

	public CreateNewVersionEvent(String codelistId, String newVersion) {
		this.codelistId = codelistId;
		this.newVersion = newVersion;
	}

	public String getCodelistId() {
		return codelistId;
	}

	public String getNewVersion() {
		return newVersion;
	}

	@Override
	protected void dispatch(CreateNewVersionHandler handler) {
		handler.onCreateNewVersion(this);
	}

	@Override
	public Type<CreateNewVersionHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CreateNewVersionHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String codelistId, String newVersion) {
		source.fireEvent(new CreateNewVersionEvent(codelistId, newVersion));
	}
}
