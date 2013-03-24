package org.cotrix.importservice.tabular.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codelist;

/**
 * Describes a mapping between tabular data and a {@link Codelist}.
 * <p>
 * The mapping is defined by:
 * 
 * <ul>
 * <li>the name of the <b>code column</b>, i.e. the column that contains the codes of the codelist.
 * <li>(optional) the <b>name</b> of the target codelist. If unspecified, the name of the code column names the target
 * codelist.
 * <li>(optional) the <b>attributes</b> of the target codelist.
 * <li>(optional) <b>attribute mappings</b>, the mappings for the attributes of the codes of the codelist.
 * </ul>
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodelistMapping {

	private final String column;

	private QName name;

	private List<Attribute> attributes = new ArrayList<Attribute>();

	private List<AttributeMapping> attributeMappings = new ArrayList<AttributeMapping>();

	/**
	 * Creates an instance with the name of the code column.
	 * 
	 * @param column the name of the code column
	 */
	public CodelistMapping(String column) {
		this.column = column;
	}

	/**
	 * Returns the name of the code column for the target codelist.
	 * 
	 * @return the code column name
	 */
	public String column() {
		return column;
	}

	/**
	 * Returns the name of the target codelist.
	 * <p>
	 * This is the name of the code column by default.
	 * 
	 * @return the name
	 */
	public QName name() {
		return name == null ? new QName(column) : name;
	}

	/**
	 * Sets the name of the target codelist
	 * <p>
	 * This overrides the name of the code column as the default.
	 * 
	 * @param name the name
	 */
	public void setName(QName name) {
		this.name = name;
	}

	/**
	 * Returns the attributes of the target codelist.
	 * 
	 * @return the attributes
	 */
	public List<Attribute> attributes() {
		return attributes;
	}

	/**
	 * Sets the attributes of the target codelist.
	 * 
	 * @param attributes
	 */
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Returns the mappings for the attributes of the codes of the target codelist.
	 * 
	 * @return the attribute mappings
	 */
	public List<AttributeMapping> attributeMappings() {
		return attributeMappings;
	}

	/**
	 * Sets the mappings for the attributes of the codes of the target codelist.
	 * 
	 * @param attributeMappings
	 */
	public void setAttributeMappings(List<AttributeMapping> attributeMappings) {
		this.attributeMappings = attributeMappings;
	}

}
