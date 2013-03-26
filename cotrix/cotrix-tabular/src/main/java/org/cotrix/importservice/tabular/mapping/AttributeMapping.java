package org.cotrix.importservice.tabular.mapping;

import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.domain.utils.Constants;

/**
 * Describes a mapping between tabular data and an attribute of a code in some {@link Codelist}.
 * <p>
 * The mapping is defined by:
 * 
 * <ul>
 * <li> the name of the <b>attribute column</b>, i.e. the column that contains the value the target attribute, for each code of the codelist.
 * <li> (optional) the <b>name</b> of the target attribute. If unspecified, the name of the attribute column names the target
 * attribute.
 * <li> (optional) the <b>type</b> of the target attribute. If unspecified, {@link Constants#DEFAULT_TYPE} is used.
 * <li> (optional) the <b>language</b> of the target attribute. 
 * <li> (optional) the <b>mapping mode</b> for the values of the attribute column, {@link MappingMode#LOG} by default (#cf {@link MappingMode}). 
 * </ul>
 * 
 * @author Fabio Simeoni
 * 
 */
public class AttributeMapping {

	private final String column;
	
	private QName name;
	private QName type;
	private String language;
	
	private MappingMode mode = MappingMode.LOG;
	
	/**
	 * Creates an instance with the name of the attribute column.
	 * @param column the attribute column name
	 */
	public AttributeMapping(String column) {
		this.column = column;
	}
	
	/**
	 * Returns the name of the attribute column for the target codelist.
	 * @return the attribute column name
	 */
	public String column() {
		return column;
	}
	
	/**
	 * Returns the name of the target attribute
	 * <p>
	 * This is the name of the attribute column by default.
	 * @return the name
	 */
	public QName name() {
		return name==null?new QName(column):name;
	}
	
	/**
	 * Sets the name of the target attribute.
	 * <p>
	 * This overrides the name of the attribute column as the default.
	 * 
	 * @param name the name
	 */
	public void setName(QName name) {
		this.name = name;
	}
	
	/**
	 * Sets the type of the target attribute.
	 * @param type the type
	 */
	public void setType(QName type) {
		this.type = type;
	}
	
	/**
	 * Sets the mode for the target attribute, overriding the default {@link MappingMode#LOG}.
	 * @param mode the mode;
	 */
	public void setMode(MappingMode mode) {
		this.mode = mode;
	}
	
	/**
	 * Returns the mode for the target attribute, {@link MappingMode#LOG} by default.
	 * @return
	 */
	public MappingMode mode() {
		return mode;
	}
	
	/**
	 * Returns the type of the target attribute.
	 * @return type the type
	 */
	public QName type() {
		return type;
	}
	
	/**
	 * Returns the language of the target attribute.
	 * @return the language
	 */
	public String language() {
		return language;
	}
	
	/**
	 * Sets the language of the target attribute
	 * @param language the language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	
}
