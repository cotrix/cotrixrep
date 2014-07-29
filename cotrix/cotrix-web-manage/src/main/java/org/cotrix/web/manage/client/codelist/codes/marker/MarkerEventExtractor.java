/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

import java.util.Collections;
import java.util.List;

import org.cotrix.web.manage.client.codelist.codes.marker.event.MarkerEvent;
import org.cotrix.web.manage.client.codelist.codes.marker.event.MarkerEventParser;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkerEventExtractor {
	
	public MarkerEventExtractor NONE = new MarkerEventExtractor() {
		
		@Override
		public List<MarkerEvent> extract(String value) {
			return Collections.emptyList();
		}
	};
	
	public MarkerEventExtractor PARSE = new MarkerEventExtractor() {
		
		@Override
		public List<MarkerEvent> extract(String value) {
			return MarkerEventParser.parse(value);
		}
	};
	
	List<MarkerEvent> extract(String value);

}
