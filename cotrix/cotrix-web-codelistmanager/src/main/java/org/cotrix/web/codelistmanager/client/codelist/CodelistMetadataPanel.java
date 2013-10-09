package org.cotrix.web.codelistmanager.client.codelist;

import java.util.List;

import org.cotrix.web.codelistmanager.client.codelist.event.AttributeChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeChangedEvent.AttributeChangedHandler;
import org.cotrix.web.codelistmanager.client.data.MetadataEditor;
import org.cotrix.web.codelistmanager.client.data.MetadataProvider;
import org.cotrix.web.codelistmanager.shared.CodelistMetadata;
import org.cotrix.web.share.client.widgets.LoadingPanel;
import org.cotrix.web.share.shared.UIAttribute;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistMetadataPanel extends LoadingPanel {

	@UiTemplate("CodelistMetadataPanel.ui.xml")
	interface CodeListMetadataPanelUiBinder extends UiBinder<Widget, CodelistMetadataPanel> {
	}

	private static CodeListMetadataPanelUiBinder uiBinder = GWT.create(CodeListMetadataPanelUiBinder.class);
	
	@UiField AttributesGrid attributesGrid;
	
	@UiField TextBox nameField;
	@UiField TextBox versionField;
	
	protected ListDataProvider<UIAttribute> attributesProvider;
	
	protected CodelistMetadata metadata;

	@Inject
	protected MetadataProvider dataProvider;
	
	@Inject
	protected MetadataEditor editor;


	public CodelistMetadataPanel() {
		add(uiBinder.createAndBindUi(this));
		bind();
	}
	
	protected void bind()
	{
		attributesGrid.addAttributeChangedHandler(new AttributeChangedHandler() {
			
			@Override
			public void onAttributeChanged(AttributeChangedEvent event) {
				editor.edited(metadata);
			}
		});
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		//workaround issue #7188 https://code.google.com/p/google-web-toolkit/issues/detail?id=7188
		onResize();
		
		if (metadata == null) loadData();
	}
	
	@UiFactory
	protected AttributesGrid createAttributesGrid()
	{
		this.attributesProvider = new ListDataProvider<UIAttribute>();
		return new AttributesGrid(attributesProvider, new TextHeader("Attributes"));
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
		dataProvider.getData(new AsyncCallback<CodelistMetadata>() {
			
			@Override
			public void onSuccess(CodelistMetadata result) {
				Log.trace("retrieved metadata: "+result);
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
	
	protected void setMetadata(CodelistMetadata metadata)
	{
		this.metadata = metadata;
		nameField.setText(metadata.getName());
		versionField.setText(metadata.getVersion());
		
		List<UIAttribute> attributes = attributesProvider.getList();
		attributes.clear();
		attributes.addAll(metadata.getAttributes());
		attributesProvider.refresh();
	}
}
