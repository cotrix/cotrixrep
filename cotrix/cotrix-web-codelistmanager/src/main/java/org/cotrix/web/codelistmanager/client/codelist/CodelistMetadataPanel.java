package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.codelist.attribute.AttributeFactory;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeChangedEvent.AttributeChangedHandler;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.codelistmanager.client.data.DataEditor;
import org.cotrix.web.codelistmanager.client.data.MetadataProvider;
import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.codelistmanager.client.util.Constants;
import org.cotrix.web.share.client.widgets.HasEditing;
import org.cotrix.web.share.client.widgets.LoadingPanel;
import org.cotrix.web.share.shared.codelist.CodelistMetadata;
import org.cotrix.web.share.shared.codelist.UIAttribute;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistMetadataPanel extends LoadingPanel implements HasEditing {

	@UiTemplate("CodelistMetadataPanel.ui.xml")
	interface CodelistMetadataPanelUiBinder extends UiBinder<Widget, CodelistMetadataPanel> {
	}

	private static CodelistMetadataPanelUiBinder uiBinder = GWT.create(CodelistMetadataPanelUiBinder.class);
	
	protected static ImageResourceRenderer renderer = new ImageResourceRenderer(); 

	@UiField(provided=true) AttributesGrid attributesGrid;

	@UiField ItemToolbar toolBar;

	protected ListDataProvider<UIAttribute> attributesProvider;

	protected CodelistMetadata metadata;

	@Inject
	protected MetadataProvider dataProvider;

	protected DataEditor<UIAttribute> attributeEditor;
	
	@Inject
	protected Constants constants;

	@Inject
	public CodelistMetadataPanel( ) {
		this.attributesProvider = new ListDataProvider<UIAttribute>();
		attributesGrid = new AttributesGrid(attributesProvider, new TextHeader("Codelist attributes"), "No attributes");

		attributeEditor = DataEditor.build(this);

		setupColumns();
		
		add(uiBinder.createAndBindUi(this));
		bind();
	}
	
	private void setupColumns() {

		Column<UIAttribute, ImageResource> bulletColumn = new Column<UIAttribute, ImageResource>( new ImageResourceCell()) {

			@Override
			public ImageResource getValue(UIAttribute attribute) {
				return CotrixManagerResources.INSTANCE.bullet();
			}
		};
		attributesGrid.insertColumn(0, bulletColumn);
		attributesGrid.setColumnWidth(0, 15, Unit.PX);

	}

	protected void bind()
	{
		attributesGrid.addAttributeChangedHandler(new AttributeChangedHandler() {

			@Override
			public void onAttributeChanged(AttributeChangedEvent event) {
				attributeEditor.updated(event.getAttribute());
			}
		});

		toolBar.addButtonClickedHandler(new ButtonClickedHandler() {

			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: addNewAttribute(); break;
					case MINUS: removeSelectedAttribute(); break;
				}
			}
		});
	}

	protected void addNewAttribute()
	{
		if (metadata!=null) {
			UIAttribute attribute = AttributeFactory.createAttribute();
			attributesProvider.getList().add(attribute);
			attributesProvider.refresh();
			attributeEditor.added(attribute);
			attributesGrid.expand(attribute);
			attributesGrid.setSelectedAttribute(attribute);
		}
	}

	protected void removeSelectedAttribute()
	{
		if (metadata!=null && attributesGrid.getSelectedAttribute()!=null) {
			UIAttribute selectedAttribute = attributesGrid.getSelectedAttribute();
			attributesProvider.getList().remove(selectedAttribute);
			attributesProvider.refresh();
			attributeEditor.removed(selectedAttribute);
		}
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
		
		attributesProvider.setList(metadata.getAttributes());
		attributesProvider.refresh();
	}

	@Override
	public void setEditable(boolean editable) {
		attributesGrid.setEditable(editable);
	}
}
