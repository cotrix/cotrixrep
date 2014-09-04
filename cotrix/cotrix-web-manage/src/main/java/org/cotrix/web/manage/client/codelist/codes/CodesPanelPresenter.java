package org.cotrix.web.manage.client.codelist.codes;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.feature.AsyncCallBackWrapper;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.LifecycleState;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.NewStateEvent;
import org.cotrix.web.manage.client.codelist.SwitchPanelEvent;
import org.cotrix.web.manage.client.codelist.codes.CodesToolbar.Action;
import org.cotrix.web.manage.client.codelist.codes.CodesToolbar.ToolBarListener;
import org.cotrix.web.manage.client.codelist.codes.editor.filter.FilterWordUpdatedEvent;
import org.cotrix.web.manage.client.codelist.codes.event.MarkerHighlightEvent;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.event.ReadyEvent;
import org.cotrix.web.manage.client.data.DataSaverManager;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodesPanelPresenter implements Presenter {
	
	interface CodesPanelPresenterEventBinder extends EventBinder<CodesPanelPresenter> {}

	private CodesPanelView view;
	private String codelistId;
	private ManageServiceAsync service;
	
	@Inject
	private DataSaverManager saverManager;
	
	@Inject @CurrentCodelist
	private UICodelist codelist;

	@Inject @CodelistBus 
	private EventBus codelistBus;

	private FeatureBinder featureBinder;

	@Inject
	public CodesPanelPresenter(CodesPanelView view, @CurrentCodelist String codelistId, ManageServiceAsync service, DataSaverManager saverManager, FeatureBinder featureBinder) {
		this.view = view;
		this.codelistId = codelistId;
		this.service = service;
		this.saverManager = saverManager;
		this.featureBinder = featureBinder;
		
		bind();
		bindFeatures();
		
		loadState();
	}
	
	@Inject
	protected void bind(@CodelistBus EventBus bus, CodesPanelPresenterEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, bus);
	}
	
	private void bind()
	{
		// TOOLBAR
		CodesToolbar toolbar = view.getToolBar();
		
		toolbar.setListener(new ToolBarListener() {
			
			@Override
			public void onAction(Action action) {
				Log.trace("toolbar onAction "+action);
				switch (action) {
					case ALL_COLUMN: showAllGroupsAsColumn(); break;
					case ALL_NORMAL: view.getCodeListEditor().showAllGroupsAsNormal(); break;
					case TO_METADATA: codelistBus.fireEvent(SwitchPanelEvent.METADATA); break;
				}
			}

			@Override
			public void onMarkerMenu(MarkerType marker, boolean selected) {
				codelistBus.fireEvent(selected?MarkerHighlightEvent.ADD(marker):MarkerHighlightEvent.REMOVE(marker));
			}

			@Override
			public void onFilterWordUpdate(String word) {
				codelistBus.fireEvent(new FilterWordUpdatedEvent(word));
			}
		});
	}
	
	private void loadState() {
		view.getToolBar().showStateLoader(true);
		service.getCodelistState(codelistId, AsyncCallBackWrapper.wrap(new ManagedFailureCallback<LifecycleState>() {

			@Override
			public void onSuccess(LifecycleState result) {
				updateState(result);
			}
		}));
	}
	
	@EventHandler
	void onNewState(NewStateEvent event) {
		updateState(event.getState());
	}
	
	@EventHandler
	void onReady(ReadyEvent event) {
		showAllGroupsAsColumn();
	}
	
	private void showAllGroupsAsColumn() {
		view.getCodeListEditor().showAllGroupsAsColumn(false);
	}
	
	private void updateState(LifecycleState state) {
		view.getToolBar().setState(String.valueOf(state).toUpperCase());
		view.getToolBar().showStateLoader(false);
	}
	
	public void reloadCodes(boolean reloadHeaders) {
		view.getCodeListEditor().showAllGroupsAsColumn(false);
		view.getCodeListEditor().reload();
	}

	
	private void bindFeatures()
	{
		
		// CODELIST EDITOR
		featureBinder.bind((HasEditing)view.getCodeListEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		//ATTRIBUTES EDITOR
		featureBinder.bind(view.getAttributesEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		//LINKS EDITOR
		featureBinder.bind(view.getLinksEditor(), codelistId, ManagerUIFeature.EDIT_CODELIST);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public Widget getView() {
		return view.asWidget();
	}
}
