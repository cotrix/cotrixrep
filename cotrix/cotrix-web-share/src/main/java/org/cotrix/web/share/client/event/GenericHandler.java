/**
 * 
 */
package org.cotrix.web.share.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface GenericHandler<T> extends EventHandler {
	void onGenericEvent(GenericEvent<T> event);

}
