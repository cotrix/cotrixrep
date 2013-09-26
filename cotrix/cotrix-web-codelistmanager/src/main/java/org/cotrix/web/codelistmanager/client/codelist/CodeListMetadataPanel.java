package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.data.AsyncDataProvider;
import org.cotrix.web.codelistmanager.client.data.DataEditor;
import org.cotrix.web.codelistmanager.shared.CodeListMetadata;
import org.cotrix.web.share.client.util.LoadingPanel;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListMetadataPanel extends LoadingPanel {

	@UiTemplate("CodeListMetadataPanel.ui.xml")
	interface CodeListMetadataPanelUiBinder extends
	UiBinder<Widget, CodeListMetadataPanel> {
	}

	private static CodeListMetadataPanelUiBinder uiBinder = GWT.create(CodeListMetadataPanelUiBinder.class);
	
	@UiField TextBox nameField;
	@UiField TextBox versionField;
	
	protected CodeListMetadata metadata;

	protected AsyncDataProvider<CodeListMetadata> dataProvider;
	protected DataEditor<CodeListMetadata> editor;

	public CodeListMetadataPanel() {
		add(uiBinder.createAndBindUi(this));
	}
	
	public void setDataProvider(AsyncDataProvider<CodeListMetadata> dataProvider)
	{
		this.dataProvider = dataProvider;
		loadData();
	}

	public void setEditor(DataEditor<CodeListMetadata> editor) {
		this.editor = editor;
	}

	@UiHandler("nameField")
	protected void nameUpdated(ValueChangeEvent<String> changedEvent)
	{
		metadata.setName(changedEvent.getValue());
		editor.edited(metadata);
	}
	
	public void loadData()
	{
		showLoader();
		dataProvider.getData(new AsyncCallback<CodeListMetadata>() {
			
			@Override
			public void onSuccess(CodeListMetadata result) {
				setMetadata(result);
				hideLoader();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading metadata", caught);
				hideLoader();
			}
		});
	}
	
	protected void setMetadata(CodeListMetadata metadata)
	{
		this.metadata = metadata;
		nameField.setText(metadata.getName());
		versionField.setText(metadata.getVersion());
	}
}
