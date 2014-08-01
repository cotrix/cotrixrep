package org.cotrix.web.manage.client.codelist.metadata;

import static org.cotrix.web.common.client.async.AsyncUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.async.AsyncUtils.SuccessCallback;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier.Void;
import org.cotrix.web.manage.client.AsyncManageServiceAsync;
import org.cotrix.web.manage.client.codelist.CodelistTaskCompleteEvent;
import org.cotrix.web.manage.client.codelist.metadata.tasks.TaskPanel;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.HeaderBuilder;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TasksPanel extends LoadingPanel {

	interface TasksPanelEventBinder extends EventBinder<TasksPanel> {};
	interface TasksPanelUiBinder extends UiBinder<Widget, TasksPanel> {}
	

	private static TasksPanelUiBinder uiBinder = GWT.create(TasksPanelUiBinder.class);
	
	@UiField HTML header;
	
	@UiField VerticalPanel tasksPanelsContainer;
	
	@Inject
	private HeaderBuilder headerBuilder;

	@Inject
	protected CotrixManagerResources resources;
	
	@Inject
	private AsyncManageServiceAsync service;
	
	@Inject @CurrentCodelist 
	private UICodelist codelist;
	
	@Inject @CodelistBus
	private EventBus eventBus;
	
	private List<TaskPanel> taskPanels = new ArrayList<TaskPanel>();

	@Inject
	public void init() {

		add(uiBinder.createAndBindUi(this));
		
		addTasks();
		
		header.setHTML(headerBuilder.getHeader("Tasks", codelist.getName().getLocalPart()));
	}
	
	private void addTasks() {
		TaskPanel changelogTaskPanel = new TaskPanel("CHANGELOG", "recalculate all changes in this version.");
		changelogTaskPanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				generateChangelog();
			}
		});
		taskPanels.add(changelogTaskPanel);
		tasksPanelsContainer.add(changelogTaskPanel);
		
		TaskPanel validationTaskPanel = new TaskPanel("VALIDATION", "recheck all schema constraints.");
		validationTaskPanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				validate();				
			}
		});
		taskPanels.add(validationTaskPanel);
		tasksPanelsContainer.add(validationTaskPanel);
		
	}
	
	public void validate() {
		Log.trace("validate");
		service.validateCodelist(codelist.getId(), async(ignoreCancel(manageError(new SuccessCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new CodelistTaskCompleteEvent(CodelistTaskCompleteEvent.Task.VALIDATION));
			}
		}))));
	}
	
	public void generateChangelog() {
		Log.trace("generateChangelog");
		service.generateCodelistChangelog(codelist.getId(), async(ignoreCancel(manageError(new SuccessCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new CodelistTaskCompleteEvent(CodelistTaskCompleteEvent.Task.CHANGELOG_GENERATION));
			}
		}))));
	}

	@Inject
	protected void bind(@CurrentCodelist String codelistId, FeatureBinder featureBinder)
	{
		featureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				setEnabled(active);
			}
		},  codelistId, ManagerUIFeature.EDIT_CODELIST);
	}
	
	private void setEnabled(boolean enabled) {
		for (TaskPanel panel:taskPanels) panel.setEnabled(enabled);
	}
	
	@Inject
	protected void bind(@CodelistBus EventBus codelistBus, TasksPanelEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, codelistBus);
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
}
