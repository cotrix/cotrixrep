/**
 * 
 */
package org.cotrix.web.manage.client.resources;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class LanguageResources {
	
	private static Map<String, ImageResource> images = new HashMap<String, ImageResource>();
	static {
		CotrixManagerResources res = CotrixManagerResources.INSTANCE;
		images.put("ar", res.ar());
		images.put("en", res.en());
		images.put("es", res.es());
		images.put("fr", res.fr());
		images.put("ru", res.ru());
		images.put("zh", res.zh());
	}
	
	public static ImageResource getResource(String language)
	{
		return images.get(language);
	}

}
