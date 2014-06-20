/**
 * 
 */
package org.cotrix.web.common.client.widgets.cell;

import org.cotrix.web.common.shared.codelist.UIQName;

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
public class UIQNameRenderer implements SafeHtmlRenderer<UIQName> {

	interface UIQNameRendererTemplate extends SafeHtmlTemplates {
		@Template("<div title=\"{0}\">{1}</div>")
		SafeHtml wrapTootTip(String toolTipText, String value);
	}

	private static final UIQNameRendererTemplate template = GWT.create(UIQNameRendererTemplate.class);

	private boolean showLocal;
	
	public UIQNameRenderer() {
		this(false);
	}

	/**
	 * @param showLocal
	 */
	public UIQNameRenderer(boolean showLocal) {
		this.showLocal = showLocal;
	}

	@Override
	public SafeHtml render(UIQName object) {
		if (object == null) return SafeHtmlUtils.EMPTY_SAFE_HTML;

		String fullName = object.toHtml();
		if (showLocal) return template.wrapTootTip(fullName, object.getLocalPart()); 
		return SafeHtmlUtils.fromString(fullName);
	}

	@Override
	public void render(UIQName object, SafeHtmlBuilder builder) {
		builder.append(render(object));
	}

}