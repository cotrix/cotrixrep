/**
 * 
 */
package org.cotrix.web.common.client.widgets.button;

import org.cotrix.web.common.client.widgets.button.ButtonResourceBuilderGrammar.*;

import com.google.gwt.resources.client.ImageResource;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ButtonResourceBuilder implements UpFace, Hover, Disabled, Title, Build, DownFace {
	
	public static UpFace create() {
		return new ButtonResourceBuilder();
	}
	
	private ButtonResources resource;
	
	private ButtonResourceBuilder() {
		resource = new ButtonResources();
	}
	
	@Override
	public Hover upFace(ImageResource upFace) {
		resource.setUpFace(upFace);
		return this;
	}

	@Override
	public DownFace disabled(ImageResource disabled) {
		resource.setDisabled(disabled);
		return this;
	}
	
	@Override
	public Disabled hover(ImageResource hover) {
		resource.setHover(hover);
		return this;
	}
	
	@Override
	public ButtonResources build() {
		return resource;
	}
	
	@Override
	public Build title(String title) {
		resource.setTitle(title);
		return this;
	}

	@Override
	public Title downFace(ImageResource downFace) {
		resource.setDownFace(downFace);
		return this;
	}

}
