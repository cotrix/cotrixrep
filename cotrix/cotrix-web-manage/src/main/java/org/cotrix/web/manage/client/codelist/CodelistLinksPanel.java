package org.cotrix.web.manage.client.codelist;

import java.util.List;

import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.codelist.link.LinksPanel;
import org.cotrix.web.manage.client.codelist.link.LinksPanel.LinksPanelListener;
import org.cotrix.web.manage.client.data.CodeLink;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Constants;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistLinksPanel extends LoadingPanel implements HasEditing {

	@UiTemplate("CodelistLinksPanel.ui.xml")
	interface CodelistLinksPanelUiBinder extends UiBinder<Widget, CodelistLinksPanel> {}
	interface CodelistLinksPanelEventBinder extends EventBinder<CodelistLinksPanel> {}

	protected static ImageResourceRenderer renderer = new ImageResourceRenderer(); 

	@Inject
	@UiField(provided=true) LinksPanel linksPanel;

	@UiField ItemToolbar toolBar;

	@Inject
	protected ManageServiceAsync service;

	@Inject @CurrentCodelist
	protected String codelistId;

	protected DataEditor<CodeLink> linkEditor;
	protected UICode currentCode;

	@Inject
	protected Constants constants;

	@Inject
	protected CotrixManagerResources resources;

	@Inject
	public void init(CodelistLinksPanelUiBinder uiBinder) {
		linkEditor = DataEditor.build(this);
		add(uiBinder.createAndBindUi(this));
		
		linksPanel.setListener(new LinksPanelListener() {
			
			@Override
			public void onUpdate(UILink link) {
				Log.trace("onUpdate link: "+link);
				linkEditor.updated(new CodeLink(currentCode, link));
			}
			
			@Override
			public void onCreate(UILink link) {
				Log.trace("onCreate link: "+link);
				currentCode.addLink(link);
				linkEditor.added(new CodeLink(currentCode, link));
			}
		});
		
		toolBar.addButtonClickedHandler(new ButtonClickedHandler() {

			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: addNewLink(); break;
					case MINUS: removeSelectedLink(); break;
				}
			}
		});
	}
	
	
	@Inject
	protected void bind(CodelistLinksPanelEventBinder binder, @EditorBus EventBus editorBus) {
		binder.bindEventHandlers(this, editorBus);
	}

	@Inject
	protected void bind(@CurrentCodelist String codelistId)
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
	}
	
	protected void addNewLink()
	{
		if (currentCode!=null) linksPanel.addNewLink();
	}

	protected void removeSelectedLink()
	{
		UILink selectedLink = linksPanel.getSelectedType();
		if (selectedLink!=null && currentCode!=null) {
			currentCode.removeLink(selectedLink);
			linksPanel.removeLink(selectedLink);
			linkEditor.removed(new CodeLink(currentCode, selectedLink));
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
	}
	
	@EventHandler
	void onCodeSelected(CodeSelectedEvent event) {
		currentCode = event.getCode();
		setLinks(currentCode.getLinks());
	}

	protected void setLinks(List<UILink> links)
	{
		linksPanel.clear();
		for (UILink link:links) linksPanel.addLink(link);
	}

	@Override
	public void setEditable(boolean editable) {
		linksPanel.setEditable(editable);
	}
}
