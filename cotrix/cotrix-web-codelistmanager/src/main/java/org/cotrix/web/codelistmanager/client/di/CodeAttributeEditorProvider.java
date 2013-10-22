/**
 * 
 */
package org.cotrix.web.codelistmanager.client.di;

import org.cotrix.web.codelistmanager.client.data.CodeAttributeEditor;

import com.google.inject.Provider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeEditorProvider implements Provider<CodeAttributeEditor> {

	protected CodeAttributeEditor editor;
	
	public void generate()
	{
		editor = new CodeAttributeEditor();
	}
	
	@Override
	public CodeAttributeEditor get() {
		return editor;
	}

}
