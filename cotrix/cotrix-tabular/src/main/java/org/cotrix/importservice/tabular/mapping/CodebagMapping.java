package org.cotrix.importservice.tabular.mapping;

import static org.cotrix.domain.utils.Utils.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codebag;

/**
 * Describes a mapping between tabular data and a {@link Codebag}.
 * <p>
 * The mapping is defined by:
 * 
 * <ul>
 * <li> the <b>codelist mappings</b>, the mappings for the codelists of the target codebag. 
 * <li> (optional) the <b>name</b> of the target codebag. If unspecified, the name of the code column names the target codelist.
 * <li> (optional) the <b>attributes</b> of the target codebag. 
 * </ul>
 * 
 * @author Fabio Simeoni
 *
 */
public class CodebagMapping {

	private final List<CodelistMapping> listMappings;
	private final QName name;
	
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	/**
	 * Creates an instance with a given name and codelist mappings.
	 * @param name the name of the target codebag
	 * @param mappings the mappings for the codelist of the target codebag
	 */
	public CodebagMapping(QName name,List<CodelistMapping> mappings) {
		
		valid("codebag name",name);
		notNull("codelist mappings",mappings);
		
		this.listMappings=mappings;
		this.name=name;
	}
	
	/**
	 * Returns the name of the target codebag.
	 * @return the name
	 */
	public QName name() {
		return name;
	}
	
	/**
	 * Returns mappings of the codelist of the target codebag.
	 * @return the codelist mappings
	 */
	public List<CodelistMapping> codelistMappings() {
		return listMappings;
	}
	
	/**
	 * Returns the attributes of the target codebag.
	 * @return the attributes
	 */
	public List<Attribute> attributes() {
		return attributes;
	}

	/**
	 * Sets the attributes of the target codebag.
	 * @param attributes
	 */
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}	
}
