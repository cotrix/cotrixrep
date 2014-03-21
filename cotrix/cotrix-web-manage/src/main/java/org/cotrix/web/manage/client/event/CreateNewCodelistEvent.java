package org.cotrix.web.manage.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CreateNewCodelistEvent extends
		GwtEvent<CreateNewCodelistEvent.CreateNewCodelistEventHandler> {

	public static Type<CreateNewCodelistEventHandler> TYPE = new Type<CreateNewCodelistEventHandler>();
	
	private String name;
	private String version;

	public interface CreateNewCodelistEventHandler extends EventHandler {
		void onCreateNewCodelist(CreateNewCodelistEvent event);
	}
	
	/**
	 * @param name
	 * @param version
	 */
	public CreateNewCodelistEvent(String name, String version) {
		this.name = name;
		this.version = version;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	@Override
	protected void dispatch(CreateNewCodelistEventHandler handler) {
		handler.onCreateNewCodelist(this);
	}

	@Override
	public Type<CreateNewCodelistEventHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CreateNewCodelistEventHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String codelistId, String newVersion) {
		source.fireEvent(new CreateNewCodelistEvent(codelistId, newVersion));
	}
}
