/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataSaveFailedEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataSavedEvent;
import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.client.data.event.SavingDataEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.StatusUpdatedEvent;

import com.google.gwt.core.client.Callback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DataSaverManager {
	
	public interface CommandGenerator<T> {
		public Class<T> getType();
		public ModifyCommand generateCommand(EditType editType, T data); 
	}
	
	@Inject
	protected ModifyCommandSequencer commandSequencer;

	@ManagerBus
	@Inject
	protected EventBus managerBus;
	
	@EditorBus 
	@Inject
	protected EventBus editorBus;
	
	@CotrixBus
	@Inject
	protected EventBus cotrixBus;
	
	public <T> void register(CommandGenerator<T> generator)
	{
		editorBus.addHandler(DataEditEvent.getType(generator.getType()), new DataSaver<T>(generator));
	}
	
	protected class DataSaver<T> implements DataEditHandler<T> {
		
		protected CommandGenerator<T> generator;
		
		public DataSaver(CommandGenerator<T> generator)
		{
			this.generator = generator;
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void onDataEdit(DataEditEvent<T> event) {
			managerBus.fireEvent(new SavingDataEvent());
			cotrixBus.fireEvent(new StatusUpdatedEvent("Saving ..."));
			ModifyCommand command = generator.generateCommand(event.getEditType(), event.getData());
			commandSequencer.enqueueCommand(command, new Callback<Void, Throwable>() {
				
				@Override
				public void onSuccess(Void result) {
					managerBus.fireEvent(new DataSavedEvent());
					cotrixBus.fireEvent(new StatusUpdatedEvent("All saved"));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					managerBus.fireEvent(new DataSaveFailedEvent(reason));
				}
			});
		}
	}

}
