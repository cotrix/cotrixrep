package org.cotrix.web.codelistmanager.client.codelist;


import org.cotrix.web.codelistmanager.client.data.AsyncDataProvider;
import org.cotrix.web.codelistmanager.client.data.DataEditor;
import org.cotrix.web.codelistmanager.shared.CodeListMetadata;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListPanelView {
	public interface Presenter {

	}

	Widget asWidget();

	public abstract void setProvider(CodeListRowDataProvider dataProvider);

	void setMetadataProvider(AsyncDataProvider<CodeListMetadata> dataProvider);

	void setMetadataEditor(DataEditor<CodeListMetadata> editor);

}
