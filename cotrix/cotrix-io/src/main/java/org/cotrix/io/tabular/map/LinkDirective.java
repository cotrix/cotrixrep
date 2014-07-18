package org.cotrix.io.tabular.map;



import javax.xml.namespace.QName;

import org.cotrix.domain.utils.LinkTemplate;

/**
 * Directives to map codelist attributes onto table columns.
 * 
 * 
 * @author Fabio Simeoni
 * 
 */
public class LinkDirective {

	private final LinkTemplate template;
	private QName columnName;
	
	
	public static LinkDirective map(LinkTemplate template) {
		return new LinkDirective(template);
	}
	
	
	public LinkDirective(LinkTemplate template) {
		this.template = template;
	}
	
	
	public LinkTemplate template() {
		return template;
	}
	
	
	public QName columnName() {
		return columnName==null?template.qname():columnName;
	}
	
	
	public LinkDirective to(QName name) {
		this.columnName=name;
		return this;
	}
	
	public LinkDirective to(String name) {
		return to(new QName(name));
	}
	
}
