package org.cotrix.web.manage.client.codelist.metadata;

import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import java.util.List;

import org.cotrix.web.common.client.async.AsyncUtils;
import org.cotrix.web.common.client.async.AsyncUtils.SuccessCallback;
import org.cotrix.web.common.client.event.CodeListPublishedEvent;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.factory.UIFactories;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.NewStateEvent;
import org.cotrix.web.manage.client.codelist.common.RemoveItemController;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.side.SidePanel;
import org.cotrix.web.manage.client.codelist.event.ReadyEvent;
import org.cotrix.web.manage.client.codelist.metadata.logbook.LogbookEntryEditingPanelFactory;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.event.NewCodelistVersionCreatedEvent;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.HeaderBuilder;
import org.cotrix.web.manage.shared.ManagerUIFeature;
import org.cotrix.web.manage.shared.UILogbookEntry;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.google.web.bindery.event.shared.binder.GenericEvent;
import com.google.web.bindery.event.shared.binder.impl.GenericEventHandler;
import com.google.web.bindery.event.shared.binder.impl.GenericEventType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LogbookPanel extends LoadingPanel implements HasEditing {

	interface LogbookPanelEventBinder extends EventBinder<LogbookPanel> {};

	@Inject
	private SidePanel panel;

	private ItemsEditingPanel<UILogbookEntry> entriesGrid;
	
	@Inject
	private HeaderBuilder headerBuilder;
	
	@Inject
	private ManageServiceAsync service;
	
	@Inject @CurrentCodelist
	private String codelistId;

	@Inject
	private CotrixManagerResources resources;

	@Inject
	private LogbookEntryEditingPanelFactory editingPanelFactory;
	
	@Inject
	private UIFactories factories;
	
	private List<UILogbookEntry> entries;
	
	@Inject
	private RemoveItemController entryRemotionController;

	@Inject
	public void init(@CurrentCodelist UICodelist codelist) {

		entriesGrid = new ItemsEditingPanel<UILogbookEntry>("No entries", editingPanelFactory);
		panel.setContent(entriesGrid);
		
		panel.getToolBar().setButtonResource(ItemButton.MINUS, RED_MINUS);
		panel.getToolBar().setButtonResource(ItemButton.PLUS, RED_PLUS);

		add(panel);

		panel.getToolBar().setVisible(ItemButton.PLUS, false);
		panel.getToolBar().addButtonClickedHandler(new ButtonClickedHandler() {

			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: break;
					case MINUS: removeSelectedEntry(); break;
				}
			}
		});

		entriesGrid.setListener(new ItemsEditingListener<UILogbookEntry>() {

			@Override
			public void onUpdate(UILogbookEntry item) {
			}

			@Override
			public void onSwitch(UILogbookEntry item, SwitchState state) {
			}

			@Override
			public void onCreate(UILogbookEntry item) {
			}
		});
		
		entriesGrid.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedEntryChanged();
			}
		});
		
		panel.setHeader("Logbook", codelist.getName().getLocalPart(), resources.definitions().LOGBOOK_RED());

	}

	@Inject
	protected void bind(@CurrentCodelist String codelistId, FeatureBinder featureBinder)
	{

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				Log.trace("REMOVE_LOGBOOKENTRY feature: "+active);
				entryRemotionController.setUserCanEdit(active);
				//we animate only if the user obtain the edit permission
				updateRemoveButtonVisibility(active);
			}
		}, codelistId, ManagerUIFeature.REMOVE_LOGBOOKENTRY);
	}
	
	@Inject
	protected void bind(@CodelistBus EventBus codelistBus, LogbookPanelEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, codelistBus);
	}
	
	@Inject
	protected void bind(@ManagerBus EventBus managerBus, @CotrixBus EventBus cotrixBus) {
		managerBus.addHandler(GenericEventType.getTypeOf(NewCodelistVersionCreatedEvent.class), new GenericEventHandler() {
			
			@Override
			public void handleEvent(GenericEvent event) {
				NewCodelistVersionCreatedEvent newCodelistVersionCreatedEvent = (NewCodelistVersionCreatedEvent) event;
				if (newCodelistVersionCreatedEvent.getOldCodelistId().equals(codelistId)) loadData();
			}
		});
		cotrixBus.addHandler(CodeListPublishedEvent.TYPE, new CodeListPublishedEvent.CodeListPublishedHandler() {
			
			@Override
			public void onCodeListPublished(CodeListPublishedEvent event) {
				if (event.getCodelistId().equals(codelistId)) loadData();
			}
		});
	}
	
	private void updateRemoveButtonVisibility(boolean animate) {
		panel.getToolBar().setEnabled(ItemButton.MINUS, entryRemotionController.canRemove(), animate);
	}

	protected void removeSelectedEntry()
	{
		if (entries!=null && entriesGrid.getSelectedItem()!=null) {
			UILogbookEntry selectedEntry = entriesGrid.getSelectedItem();
			if (!selectedEntry.isRemovable()) return; 
			entriesGrid.removeItem(selectedEntry);
			entries.remove(selectedEntry);
			selectedEntryChanged();
			service.removeLogbookEntry(codelistId, selectedEntry.getId(), AsyncUtils.manageError(new SuccessCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
				}
			}));
		}
	}
	

	private void selectedEntryChanged()
	{
		Log.trace("selectedEntryChanged "+entriesGrid.getSelectedItem());
		if (entriesGrid.getSelectedItem()!=null) {
			UILogbookEntry entry = entriesGrid.getSelectedItem();
			entryRemotionController.setItemCanBeRemoved(entry.isRemovable());
		} else entryRemotionController.setItemCanBeRemoved(false);
		updateRemoveButtonVisibility(false);
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
	public void onReady(ReadyEvent event) {
		loadData();
	}
	
	@EventHandler
	public void onNewState(NewStateEvent event) {
		loadData();
	}

	public void loadData()
	{
		showLoader();
		service.getLogbookEntries(codelistId, new AsyncCallback<List<UILogbookEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading logbook entries", caught);
				hideLoader();
			}

			@Override
			public void onSuccess(List<UILogbookEntry> result) {
				Log.trace("retrieved entries: "+result);
				setEntries(result);
				hideLoader();
				
			}
			
		});
	}

	protected void setEntries(List<UILogbookEntry> entries)
	{
		this.entries = entries;
		entriesGrid.clear();
		for (UILogbookEntry entry:entries) {
			entriesGrid.addItemPanel(entry);
		}
	}

	@Override
	public void setEditable(boolean editable) {
		entriesGrid.setEditable(editable);
	}
}
