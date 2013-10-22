/**
 * 
 */
package org.cotrix.web.codelistmanager.client.di;

import org.cotrix.web.codelistmanager.client.data.MetadataAttributeEditor;

import com.google.inject.Provider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataAttributeEditorProvider implements Provider<MetadataAttributeEditor> {

	protected MetadataAttributeEditor editor;
	
	public void generate()
	{
		editor = new MetadataAttributeEditor();
	}
	
	@Override
	public MetadataAttributeEditor get() {
		return editor;
	}

}
