/**
 * 
 */
package org.cotrix.web.share.client.util;

import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.StatusUpdatedEvent;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class StatusUpdates {
	
	@Inject
	protected static @CotrixBus EventBus eventBus;
	
	public static void statusUpdate(String status)
	{
		eventBus.fireEvent(new StatusUpdatedEvent(status));
	}

}
