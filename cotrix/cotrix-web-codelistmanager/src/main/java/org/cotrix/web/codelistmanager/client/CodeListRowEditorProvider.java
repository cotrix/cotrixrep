/**
 * 
 */
package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.data.CodeListRowEditor;

import com.google.inject.Provider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListRowEditorProvider implements Provider<CodeListRowEditor> {

	protected CodeListRowEditor editor;
	
	public void generate()
	{
		editor = new CodeListRowEditor();
	}
	
	@Override
	public CodeListRowEditor get() {
		return editor;
	}

}
