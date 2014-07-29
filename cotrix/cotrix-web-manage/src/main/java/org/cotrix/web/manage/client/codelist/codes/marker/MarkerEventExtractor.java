/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cotrix.web.manage.client.resources.CotrixManagerResources;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkerEventExtractor {
	
	public MarkerEventExtractor NONE = new MarkerEventExtractor() {
		
		@Override
		public List<MarkerEvent> extract(String value) {
			
			/*MarkerEvent event = new MarkerEvent();
			event.setIcon(CotrixManagerResources.INSTANCE.codes());
			event.setTitle("ENGLISH_NAME");
			event.setSubTitle("[name,en]");
			event.setDescription("occurs 2 time(s), violating occurrence constraint [[min=1]]");
			event.setTimestamp("federico on July 28, 2014 1:34:11 PM");
			
			MarkerEvent event2 = new MarkerEvent();
			event2.setIcon(CotrixManagerResources.INSTANCE.codes());
			event2.setTitle("FAMILY");
			event2.setSubTitle("[name,en]");
			event2.setDescription("foo violates contraint x y z");
			event2.setTimestamp("federico on July 29, 2014 12:31:10 AM");
			
			return Arrays.asList(event, event2);*/
			return Collections.emptyList();
		}
	};
	
	List<MarkerEvent> extract(String value);

}
