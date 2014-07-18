package org.cotrix.io.tabular.map;



import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.LinkDefinition;

/**
 * Directives to map codelist attributes onto table columns.
 * 
 * 
 * @author Fabio Simeoni
 * 
 */
public class LinkDirective {

	private final LinkDefinition def;
	private QName columnName;
	
	
	public static LinkDirective map(LinkDefinition def) {
		return new LinkDirective(def);
	}
	
	
	public LinkDirective(LinkDefinition def) {
		this.def = def;
	}
	
	
	public LinkDefinition definition() {
		return def;
	}
	
	
	public QName columnName() {
		return columnName==null?def.qname():columnName;
	}
	
	
	public LinkDirective to(QName name) {
		this.columnName=name;
		return this;
	}
	
	public LinkDirective to(String name) {
		return to(new QName(name));
	}
	
}
