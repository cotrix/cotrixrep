/**
 * 
 */
package org.cotrix.web.codelistmanager.client.di;

import org.cotrix.web.codelistmanager.client.data.CodeEditor;

import com.google.inject.Provider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistRowEditorProvider implements Provider<CodeEditor> {

	protected CodeEditor editor;
	
	public void generate()
	{
		editor = new CodeEditor();
	}
	
	@Override
	public CodeEditor get() {
		return editor;
	}

}
