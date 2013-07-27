package org.cotrix.io.tabular;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codelist;
import org.cotrix.io.map.MapDirectives;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

/**
 * {@link ImportDirectives} for data available in some tabular format.
 * <p>
 * The directives define how the data should be mapped onto a {@link Codelist}. The mapping is defined by:
 * 
 * <ul>
 * <li>the <b>code column</b>, i.e. the column that contains the codes of the target codelist.
 * <li>(optional) the <b>name</b> of the target codelist. If unspecified, the codelist is named after the code column.
 * <li>(optional) the <b>attributes</b> of the target codelist.
 * <li>(optional) <b>column directives</b>, the directives for mapping other columns.
 * <li> (optional) the <b>mapping mode</b> for missing values in the code column, {@link MappingMode#STRICT} by default (#cf {@link MappingMode}).
 * </ul>
 */
public class TableMapDirectives implements MapDirectives<Table> {


	private final Column column;

	private QName name;

	private List<Attribute> attributes = new ArrayList<Attribute>();

	private List<ColumnDirectives> columnDirectives = new ArrayList<ColumnDirectives>();

	private MappingMode mode = MappingMode.STRICT;
	

	/**
	 * Creates an instance with the code column.
	 * 
	 * @param column the code column name
	 */
	public TableMapDirectives(Column column) {
		this.column = column;
	}
	
	/**
	 * Creates an instance with the name of the code column.
	 * 
	 * @param column the code column name
	 */
	public TableMapDirectives(QName name) {
		this(new Column(name));
	}
	
	/**
	 * Creates an instance with the name of the code column.
	 * 
	 * @param column the code column name
	 */
	public TableMapDirectives(String column) {
		this(new Column(column));
	}

	/**
	 * Returns the code column for these directives.
	 * 
	 * @return the code column name
	 */
	public Column codeColumn() {
		return column;
	}

	/**
	 * Returns the name of the target codelist for these directives.
	 * <p>
	 * By default, this is the name of the code column.
	 * 
	 * @return the name
	 */
	public QName name() {
		return name == null ? column.name(): name;
	}

	/**
	 * Sets the name of the target codelist for these directives.
	 * <p>
	 * This overrides the name of the code column as the default.
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public TableMapDirectives name(QName name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Sets the name of the target codelist for these directives.
	 * <p>
	 * This overrides the name of the code column as the default.
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public TableMapDirectives name(String name) {
		return name(new QName(name));
	}

	/**
	 * Returns the attributes of the target codelist for these directives.
	 * 
	 * @return the attributes
	 */
	public List<Attribute> attributes() {
		return attributes;
	}

	/**
	 * Sets the attributes of the target codelist for these directives.
	 * 
	 * @param attributes
	 * @return these directives
	 */
	public TableMapDirectives attributes(List<Attribute> attributes) {
		this.attributes = attributes;
		return this;
	}
	
	/**
	 * Sets the attributes of the target codelist for these directives.
	 * 
	 * @param attributes
	 * @return these directives
	 */
	public TableMapDirectives attributes(Attribute ... attributes) {
		return attributes(asList(attributes));
	}
	
	
	/**
	 * Adds a {@link ColumnDirectives} to these directives.
	 * 
	 * @param the column directive
	 * @return these directives
	 */
	public TableMapDirectives add(ColumnDirectives directive) {
		 columns().add(directive);
		 return this;
	}
	
	/**
	 * Returns the {@link ColumnDirectives}s of these directives.
	 * 
	 * @return the column directives
	 */
	public List<ColumnDirectives> columns() {
		return columnDirectives;
	}
	
	/**
	 * Sets the mode for these directives, overriding the default {@link MappingMode#STRICT}.
	 * @param mode the mode
	 * @return these directives
	 */
	public TableMapDirectives mode(MappingMode mode) {
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
