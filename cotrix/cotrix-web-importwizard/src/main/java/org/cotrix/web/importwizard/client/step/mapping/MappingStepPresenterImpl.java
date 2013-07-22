package org.cotrix.web.importwizard.client.step.mapping;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingUpdatedEvent.MappingUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.shared.ColumnDefinition;
import org.cotrix.web.importwizard.shared.ColumnType;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingStepPresenterImpl extends AbstractWizardStep implements MappingStepPresenter, MappingUpdatedHandler {

	protected MappingStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public MappingStepPresenterImpl(MappingStepView view, @ImportBus EventBus importEventBus){
		super("mapping", "Define Type", "Define Type", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(MappingUpdatedEvent.TYPE, this);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public boolean isComplete() {
		return validate();
	}
	
	protected boolean validate()
	{
		List<ColumnDefinition> columns = view.getColumns();
		
		//only one code
		int codeCount = 0;
		for (ColumnDefinition column:columns) if (column.getType()==ColumnType.CODE) codeCount++;
		
		if (codeCount==0) {
			view.alert("One code column required");
			return false;
		}
		
		if (codeCount>1) {
			view.alert("You can assign only one code.");
			return false;
		}
		
		return true;
	}

	@Override
	public void onMappingUpdated(MappingUpdatedEvent event) {
		if (event.isUserEdit()) return;
		view.setColumns(event.getColumns());
	}
}
