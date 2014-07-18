package org.cotrix.io.tabular.map;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.io.MapService.MapDirectives;
import org.virtualrepository.tabular.Table;

/**
 * Directives for mapping codelists into tables.
 * <p>
 * The mapping is defined by:
 * 
 * <ul>
 * <li>(optional) the <b>name</b> of the target codelist. If unspecified, the codelist is named after the code column.
 * <li>(optional) the <b>attributes</b> of the target codelist.
 * <li>(optional) <b>column directives</b>, the directives for mapping other columns.
 * <li> (optional) the <b>mapping mode</b> for missing values in the code column, {@link MappingMode#STRICT} by default (#cf {@link MappingMode}).
 * </ul>
 */
public class Codelist2TableDirectives implements MapDirectives<Table> {


	private QName codeColumnName;
	
	private List<AttributeDirective> attributeDirectives = new ArrayList<AttributeDirective>();
	private List<LinkDirective> linkDirectives = new ArrayList<LinkDirective>();
	
	private MappingMode mode = MappingMode.STRICT;
	

	public Codelist2TableDirectives() {}


	public QName codeColumnName() {
		return codeColumnName;
	}

	public Codelist2TableDirectives codeColumnName(String name) {
		return codeColumnName(new QName(name));
	}
	
	public Codelist2TableDirectives codeColumnName(QName name) {
		this.codeColumnName=name;
		return this;
	}
	
	
	public Codelist2TableDirectives add(AttributeDirective directive) {
		 attributeDirectives.add(directive);
		 return this;
	}
	
	public Codelist2TableDirectives add(LinkDirective directive) {
		 linkDirectives.add(directive);
		 return this;
	}
	
	public Codelist2TableDirectives add(AttributeDefinition def) {
		 return add(new AttributeDirective(def));
	}
	
	public Codelist2TableDirectives add(Attribute a) {
		 return add(a.definition());
	}
	
	public Codelist2TableDirectives add(LinkDefinition link) {
		return add(new LinkDirective(link));
	}
	
	public Codelist2TableDirectives add(Codelink link) {
		return add(link.definition());
	}
	
	public List<AttributeDirective> attributes() {
		return attributeDirectives;
	}
	
	public List<LinkDirective> links() {
		return linkDirectives;
	}
	
	public Codelist2TableDirectives mode(MappingMode mode) {
		this.mode = mode;
		return this;
	}
	
	public MappingMode mode() {
		return mode;
	}
}
