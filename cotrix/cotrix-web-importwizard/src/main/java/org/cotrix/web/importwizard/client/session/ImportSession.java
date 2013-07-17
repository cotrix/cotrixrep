/**
 * 
 */
package org.cotrix.web.importwizard.client.session;

import org.cotrix.web.importwizard.client.session.SourceTypeChangeEvent.HasSourceTypeChangeHandlers;
import org.cotrix.web.importwizard.client.session.SourceTypeChangeEvent.SourceTypeChangeHandler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportSession implements HasSourceTypeChangeHandlers {
	
	protected HandlerManager handlerManager;
	
	protected SourceType sourceType;
	
	public ImportSession()
	{
		handlerManager = new HandlerManager(this);
	}

	/**
	 * @return the sourceType
	 */
	public SourceType getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
		SourceTypeChangeEvent.fire(handlerManager, sourceType);
	}

	/**
	 * @param event
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);		
	}

	/**
	 * @param handler
	 * @return
	 */
	@Override
	public HandlerRegistration addSourceTypeChangeHandler(SourceTypeChangeHandler handler) {
		return handlerManager.addHandler(SourceTypeChangeEvent.TYPE, handler);
	}

}
