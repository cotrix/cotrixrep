package org.cotrix.io.tabular.map;

import java.text.AttributedCharacterIterator.Attribute;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.utils.Constants;
import org.virtualrepository.tabular.Column;

/**
 * Import directives for  {@link Column}s of codelists available in tabular format.
 * <p>
 * 
 * The directives define a mapping between the column and an {@link Attribute} of a {@link Code} or {@link Codelist}.
 * The mapping is defined by:
 * 
 * <ul>
 * <li> (optional) the <b>name</b> of the target attribute. If unspecified, the attribute is named as the column.
 * <li> (optional) the <b>type</b> of the target attribute. If unspecified,  the attribute has the {@link Constants#defaultType}.
 * <li> (optional) the <b>language</b> of the target attribute. 
 * <li> (optional) the <b>mapping mode</b> for missing values in the column, {@link MappingMode#log} by default (#cf {@link MappingMode}). 
 * </ul>
 * 
 * @author Fabio Simeoni
 * 
 */
public class ColumnDirectives {

	private final Column column;
	
	private QName name;
	private QName type;
	private String language;
	private boolean required = false;
	
	private MappingMode mode = MappingMode.log;
	
	
	/**
	 * Creates mapping directives for a given column.
	 * @param name the column name
	 * @return the directives
	 */
	public static ColumnDirectives column(String name) {
		return column(new QName(name));
	}
	
	/**
	 * Creates mapping directives for a given column.
	 * @param name the column name
	 * @return the directives
	 */
	public static ColumnDirectives column(QName name) {
		return new ColumnDirectives(new Column(name));
	}
	
	
	//constructors
	
	/**
	 * Creates an instance for a given column.
	 * @param column the column
	 */
	public ColumnDirectives(Column column) {
		this.column = column;
	}
	
	/**
	 * Returns the column for these directives.
	 * @return the column
	 */
	public Column column() {
		return column;
	}
	
	
	/**
	 * Returns the name of the target attribute
	 * <p>
	 * By default, this is the name of the column.
	 * 
	 * @return the name of the attribute
	 */
	public QName name() {
		return name==null?column.name():name;
	}
	
	
	/**
	 * Sets the name of the target attribute
	 * 
	 * @param the name
	 * @return these directives
	 */
	public ColumnDirectives name(QName name) {
		this.name=name;
		return this;
	}
	
	
	public boolean required() {
		return required;
	}
	
	public void required(boolean required) {
		this.required=required;
	}
	
	/**
	 * Sets the name of the target attribute
	 * 
	 * @param the name
	 * @return these directives
	 */
	public ColumnDirectives name(String name) {
		return name(new QName(name));
	}

	/**
	 * Sets the type of the target attribute.
	 * 
	 * @param type the type
	 * @return these directives
	 */
	public ColumnDirectives type(String type) {
		return type(new QName(type));
	}
	
	/**
	 * Sets the type of the target attribute.
	 * 
	 * @param type the type
	 * @return these directives
	 */
	public ColumnDirectives type(QName type) {
		this.type = type;
		return this;
	}
	
	/**
	 * Sets the mode of these directives.
	 * <p>
	 * By default, the mode is {@link MappingMode#log}.
	 * 
	 * @param mode the mode
	 * @return these directives
	 */
	public ColumnDirectives mode(MappingMode mode) {
		this.mode = mode;
		return this;
	}
	
	/**
	 * Returns the mode of these directives.
	 * @return the mode
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
	 * @return the language of the attribute
	 */
	public String language() {
		return language;
	}
	
	/**
	 * Sets the language of the target attribute
	 * @param language the language of the attribute
	 * @return these directives
	 */
	public ColumnDirectives language(String language) {
		this.language = language;
		return this;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ColumnDirectives [column=");
		builder.append(column);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", language=");
		builder.append(language);
		builder.append(", mode=");
		builder.append(mode);
		builder.append("]");
		return builder.toString();
	}
}
