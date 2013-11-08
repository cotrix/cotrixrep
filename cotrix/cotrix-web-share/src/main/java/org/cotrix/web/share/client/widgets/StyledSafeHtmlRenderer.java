/**
 * 
 */
package org.cotrix.web.share.client.widgets;

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
	}
	
	protected static final Template template = GWT.create(Template.class);
	protected String style;

	/**
	 * @param style
	 */
	public StyledSafeHtmlRenderer(String style) {
		this.style = style;
	}

	@Override
	public SafeHtml render(String object) {
		return (object == null) ? SafeHtmlUtils.EMPTY_SAFE_HTML :  template.styled(SafeHtmlUtils.fromString(object), style);
	}

	@Override
	public void render(String object, SafeHtmlBuilder builder) {
		builder.append(template.styled(SafeHtmlUtils.fromString(object), style));
	}
	
}