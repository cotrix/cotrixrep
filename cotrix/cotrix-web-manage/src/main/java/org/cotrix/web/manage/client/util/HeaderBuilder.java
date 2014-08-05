/**
 * 
 */
package org.cotrix.web.manage.client.util;

import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class HeaderBuilder {
	
	interface Template extends SafeHtmlTemplates {

		@Template("<span>{0}</span>&nbsp;for&nbsp;<span class=\"{1}\">{2}</span>")
		SafeHtml header(String label, String style, String subject);
		
		@Template("<span style=\"font-weight:400;\">{0}&nbsp;for&nbsp;</span><span style=\"{1}font-weight:400;\">{2}</span>")
		SafeHtml coloredHeader(String label, SafeStyles style, String subject);
		
		@Template("<span>{0}</span>")
		SafeHtml header(String label);
	}
	
	@Inject
	private Template template;
	
	public SafeHtml getHeader(String label, String subject) {
		if (subject == null) return template.header(label);
		else return template.header(label, CotrixManagerResources.INSTANCE.css().headerCode(), subject);
	}
	
	public SafeHtml getHeader(String label, String subject, String color) {
		if (subject == null) return template.header(label);
		else return template.coloredHeader(label, SafeStylesUtils.forTrustedColor(color), subject);
	}
}
