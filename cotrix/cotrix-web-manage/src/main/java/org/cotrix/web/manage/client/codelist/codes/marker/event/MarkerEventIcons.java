/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.event;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkerEventIcons extends ClientBundle {
	
	public ImageResource addition();
	public ImageResource deletion();
	public ImageResource change();
	public ImageResource error();

}
