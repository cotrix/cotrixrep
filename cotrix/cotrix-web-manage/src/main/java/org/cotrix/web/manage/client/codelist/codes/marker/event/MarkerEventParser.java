/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.event;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerEventParser {

	interface Events {
		List<MarkerEvent> getEvents();
	}
	
	interface MarkerEventFactory extends AutoBeanFactory {
		AutoBean<MarkerEvent> event();
		AutoBean<Events> events();
	}

	private static MarkerEventFactory factory = GWT.create(MarkerEventFactory.class);

	public static List<MarkerEvent> parse(String json) {
		Log.trace("parsing: "+json);
		AutoBean<Events> bean = AutoBeanCodex.decode(factory, Events.class, wrap(json));
		Log.trace("found "+bean.as().getEvents().size());
		return bean.as().getEvents();
	}
	
	private static String wrap(String json) {
		if (json == null || json.isEmpty()) return "{\"events\":[]}";
		return "{\"events\":"+json+"}";
	}

}
