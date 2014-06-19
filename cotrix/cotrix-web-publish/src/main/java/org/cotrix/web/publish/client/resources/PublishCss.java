/**
 * 
 */
package org.cotrix.web.publish.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishCss extends CssResource {

	String mappingCell();
	String mappingAttributeCell();
	String mappingAttributeTable();
	String mappingAttributeHeaderCell();
	String mappingAttributeHeader();
	String mappingAttributeNoAttributeCell();
	
	String buttonCsv();
	String buttonSdmx();
	String buttonComet();
}
