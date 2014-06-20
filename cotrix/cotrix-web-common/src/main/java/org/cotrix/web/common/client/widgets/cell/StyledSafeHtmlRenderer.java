/**
 * 
 */
package org.cotrix.web.common.client.widgets.cell;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class StyledSafeHtmlRenderer implements SafeHtmlRenderer<String> {
	
	interface Template extends SafeHtmlTemplates {
		@Template("<div class=\"{1}\">{0}</div>")
		SafeHtml styled(SafeHtml value, String style);
		
		@Template("<div class=\"{1}\" title=\"{2}\">{0}</div>")
		SafeHtml styledWithTitle(SafeHtml value, String style, String title);
	}
	
	private static final Template template = GWT.create(Template.class);
	private String style;
	private boolean addTitle;

	/**
	 * @param style
	 */
	public StyledSafeHtmlRenderer(String style) {
		this.style = style;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public boolean isAddTitle() {
		return addTitle;
	}

	public void setAddTitle(boolean addTitle) {
		this.addTitle = addTitle;
	}

	@Override
	public SafeHtml render(String object) {
		return (object == null) ? SafeHtmlUtils.EMPTY_SAFE_HTML :  toSafeHtml(object);
	}

	@Override
	public void render(String object, SafeHtmlBuilder builder) {
		builder.append(toSafeHtml(object));
	}
	
	private SafeHtml toSafeHtml(String object) {
		return addTitle?template.styledWithTitle(SafeHtmlUtils.fromString(object), style, object):template.styled(SafeHtmlUtils.fromString(object), style);
	}
	
}