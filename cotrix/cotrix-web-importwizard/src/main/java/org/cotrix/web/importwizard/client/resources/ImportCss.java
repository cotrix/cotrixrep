/**
 * 
 */
package org.cotrix.web.importwizard.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ImportCss extends CssResource {

	String sectionTitle();
	
	String missingValueText();
	
	String wizardPanel();
	
	String previewHeader();
	String preview();
	String previewCell();
	
	String mappingTable();
	String mappingTableNameHeader();	
	String mappingCell();
	
	String buttonComputer();
	String buttonCloud();
	String buttonBrowse();
	String buttonDownload();

	String buttonImport();
}
