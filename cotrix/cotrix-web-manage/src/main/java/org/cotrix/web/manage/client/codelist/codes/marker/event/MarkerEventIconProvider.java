/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.event;

import java.util.EnumMap;

import org.cotrix.web.manage.client.codelist.codes.marker.event.MarkerEvent.Type;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerEventIconProvider {
	
	private static MarkerEventIcons icons = GWT.create(MarkerEventIcons.class);

	private static EnumMap<MarkerEvent.Type, ImageResource> images = new EnumMap<MarkerEvent.Type, ImageResource>(MarkerEvent.Type.class);
	static {
		images.put(Type.ADDITION, icons.addition());
		images.put(Type.CHANGE, icons.change());
		images.put(Type.DELETION, icons.deletion());
		images.put(Type.ERROR, icons.error());
	}
	
	public static ImageResource getIcon(Type type) {
		return images.get(type);
	}
}
