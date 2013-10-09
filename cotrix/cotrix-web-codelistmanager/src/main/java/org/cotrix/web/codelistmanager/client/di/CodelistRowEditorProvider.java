/**
 * 
 */
package org.cotrix.web.codelistmanager.client.di;

import org.cotrix.web.codelistmanager.client.data.CodelistRowEditor;

import com.google.inject.Provider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistRowEditorProvider implements Provider<CodelistRowEditor> {

	protected CodelistRowEditor editor;
	
	public void generate()
	{
		editor = new CodelistRowEditor();
	}
	
	@Override
	public CodelistRowEditor get() {
		return editor;
	}

}
