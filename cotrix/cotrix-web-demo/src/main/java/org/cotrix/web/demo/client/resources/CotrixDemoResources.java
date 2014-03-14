package org.cotrix.web.demo.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public interface CotrixDemoResources extends ClientBundle {

	@Source("style.css")
	public CotrixDemoStyle css();
	
	interface CotrixDemoStyle extends CssResource {
		String demoLabel();
}

}