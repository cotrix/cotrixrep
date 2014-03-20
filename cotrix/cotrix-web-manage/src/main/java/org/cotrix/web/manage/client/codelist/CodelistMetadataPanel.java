package org.cotrix.web.manage.client.codelist;

import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.manage.client.codelist.attribute.AttributeChangedEvent;
import org.cotrix.web.manage.client.codelist.attribute.AttributeFactory;
import org.cotrix.web.manage.client.codelist.attribute.AttributesGrid;
import org.cotrix.web.manage.client.codelist.attribute.AttributeChangedEvent.AttributeChangedHandler;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.data.MetadataProvider;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.client.util.Constants;
import org.cotrix.web.manage.shared.ManagerUIFeature;

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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
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

	protected UICodelistMetadata metadata;

	@Inject
	protected MetadataProvider dataProvider;

	protected DataEditor<UIAttribute> attributeEditor;
	
	@Inject
	protected Constants constants;
	
	@Inject
	protected CotrixManagerResources resources;

	@Inject
	public CodelistMetadataPanel( ) {
		this.attributesProvider = new ListDataProvider<UIAttribute>();
		attributesGrid = new AttributesGrid(attributesProvider, new TextHeader("Codelist attributes"), "No attributes", new MultiWordSuggestOracle());

		attributeEditor = DataEditor.build(this);

		setupColumns();
		
		add(uiBinder.createAndBindUi(this));
	}
	
	private void setupColumns() {

		Column<UIAttribute, ImageResource> bulletColumn = new Column<UIAttribute, ImageResource>( new ImageResourceCell()) {

			@Override
			public ImageResource getValue(UIAttribute attribute) {
				return resources.bullet();
			}
		};
		attributesGrid.insertColumn(0, bulletColumn);
		attributesGrid.setColumnWidth(0, 15, Unit.PX);

	}
	
	@Inject
	protected void bind(@CodelistId String codelistId)
	{
		
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.PLUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);
		
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.MINUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);
		
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
		dataProvider.getData(new AsyncCallback<UICodelistMetadata>() {

			@Override
			public void onSuccess(UICodelistMetadata result) {
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

	protected void setMetadata(UICodelistMetadata metadata)
	{
		this.metadata = metadata;
		
		Attributes.sortByAttributeType(metadata.getAttributes());
		attributesProvider.setList(metadata.getAttributes());
		attributesProvider.refresh();
	}

	@Override
	public void setEditable(boolean editable) {
		attributesGrid.setEditable(editable);
	}
}
