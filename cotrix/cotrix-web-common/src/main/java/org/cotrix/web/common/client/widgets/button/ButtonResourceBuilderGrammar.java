/**
 * 
 */
package org.cotrix.web.common.client.widgets.button;

import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ButtonResourceBuilderGrammar {
	
	public interface UpFace {
		Hover upFace(ImageResource hover);
	}
	
	public interface Hover {
		Disabled hover(ImageResource hover);
	}
	
	public interface Disabled {
		DownFace disabled(ImageResource disabled);
		Title downFace(ImageResource downFace);
		Build title(String title);
	}
	
	public interface DownFace {
		Title downFace(ImageResource downFace);
		Build title(String title);
	}
	
	public interface Title {
		Build title(String title);
	}
	
	public interface Build {
		ButtonResources build();
	}

}
