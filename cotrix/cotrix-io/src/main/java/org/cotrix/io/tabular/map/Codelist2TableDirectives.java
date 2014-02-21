package org.cotrix.io.tabular.map;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
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
	
	private List<AttributeDirectives> attributeDirectives = new ArrayList<AttributeDirectives>();

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
	
	
	public Codelist2TableDirectives add(AttributeDirectives directive) {
		 attributeDirectives.add(directive);
		 return this;
	}
	
	public Codelist2TableDirectives add(Attribute template) {
		 attributeDirectives.add(new AttributeDirectives(template));
		 return this;
	}
	
	/**
	 * Returns the {@link ColumnDirectives}s of these directives.
	 * 
	 * @return the column directives
	 */
	public List<AttributeDirectives> attributes() {
		return attributeDirectives;
	}
	
	/**
	 * Sets the mode for these directives, overriding the default {@link MappingMode#STRICT}.
	 * @param mode the mode
	 * @return these directives
	 */
	public Codelist2TableDirectives mode(MappingMode mode) {
		this.mode = mode;
		return this;
	}
	
	/**
	 * Returns the mode for these directives, {@link MappingMode#STRICT} by default.
	 * @return the mode
	 */
	public MappingMode mode() {
		return mode;
	}
}
