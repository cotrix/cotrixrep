/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.client.util.StatusUpdates;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.manage.client.data.event.DataSaveFailedEvent;
import org.cotrix.web.manage.client.data.event.DataSavedEvent;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.data.event.SavingDataEvent;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Callback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DataSaverManager {

	public interface CommandBridge<T> {
		public Class<T> getType();
		public ModifyCommand generateCommand(EditType editType, T data);
		public void handleResponse(EditType editType, T data, ModifyCommandResult response);
	}
	
	@Inject
	@CurrentCodelist
	protected String codelistId;

	@Inject
	protected ModifyCommandSequencer commandSequencer;

	@ManagerBus
	@Inject
	protected EventBus managerBus;

	@CodelistBus 
	@Inject
	protected EventBus codelistBus;

	public <T> void register(CommandBridge<T> generator)
	{
		codelistBus.addHandler(DataEditEvent.getType(generator.getType()), new DataSaver<T>(generator));
	}

	protected class DataSaver<T> implements DataEditHandler<T> {

		private CommandBridge<T> generator;

		public DataSaver(CommandBridge<T> generator)
		{
			this.generator = generator;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void onDataEdit(final DataEditEvent<T> event) {
			Log.trace("onDataEdit codelistId: "+codelistId+" event: "+event+ " for generator: "+generator);
			managerBus.fireEvent(new SavingDataEvent());
			StatusUpdates.statusSaving();
			final T data = event.getData();
			final EditType editType = event.getEditType();
			ModifyCommand command = generator.generateCommand(editType, data);
			commandSequencer.enqueueCommand(command, new Callback<ModifyCommandResult, Throwable>() {

				@Override
				public void onSuccess(ModifyCommandResult result) {
					
					generator.handleResponse(editType, data, result);

					managerBus.fireEvent(new DataSavedEvent(codelistId, event));
					StatusUpdates.statusSaved();
				}

				@Override
				public void onFailure(Throwable reason) {
					managerBus.fireEvent(new DataSaveFailedEvent(reason));
				}
			});
		}
	}
}
