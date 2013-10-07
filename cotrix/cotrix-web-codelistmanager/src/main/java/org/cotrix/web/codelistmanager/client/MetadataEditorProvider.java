/**
 * 
 */
package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.data.MetadataEditor;

import com.google.inject.Provider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataEditorProvider implements Provider<MetadataEditor> {

	protected MetadataEditor editor;
	
	public void generate()
	{
		editor = new MetadataEditor();
	}
	
	@Override
	public MetadataEditor get() {
		return editor;
	}

}
