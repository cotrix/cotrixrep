/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.client.util.StatusUpdates;
import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.codelist.CodelistId;
import org.cotrix.web.manage.client.codelist.event.CodeUpdatedEvent;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.DataSaveFailedEvent;
import org.cotrix.web.manage.client.data.event.DataSavedEvent;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.data.event.SavingDataEvent;
import org.cotrix.web.manage.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.modify.HasCode;
import org.cotrix.web.manage.shared.modify.HasId;
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

	public interface CommandGenerator<T> {
		public Class<T> getType();
		public ModifyCommand generateCommand(EditType editType, T data); 
	}
	
	@Inject
	@CodelistId
	protected String codelistId;

	@Inject
	protected ModifyCommandSequencer commandSequencer;

	@ManagerBus
	@Inject
	protected EventBus managerBus;

	@EditorBus 
	@Inject
	protected EventBus editorBus;

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
			StatusUpdates.statusSaving();
			final T data = event.getData();
			final EditType editType = event.getEditType();
			ModifyCommand command = generator.generateCommand(editType, data);
			commandSequencer.enqueueCommand(command, new Callback<ModifyCommandResult, Throwable>() {

				@Override
				public void onSuccess(ModifyCommandResult result) {

					if (editType!=EditType.REMOVE) {

						if (data instanceof Identifiable && result instanceof HasId) {
							Identifiable identifiable = (Identifiable) data;
							HasId hasId = (HasId) result;
							String id = hasId.getId();
							Log.trace("setting id ("+id+") in identifiable object "+data);
							identifiable.setId(id);
						}

						if (data instanceof HasCode && result instanceof HasCode) updateCode(((HasCode)data).getCode(), ((HasCode)result).getCode());
						if (data instanceof UICode && result instanceof HasCode) updateCode((UICode)data, ((HasCode)result).getCode());
					}

					managerBus.fireEvent(new DataSavedEvent(codelistId));
					StatusUpdates.statusSaved();
				}

				@Override
				public void onFailure(Throwable reason) {
					managerBus.fireEvent(new DataSaveFailedEvent(reason));
				}
			});
		}

		protected void updateCode(UICode code, UICode updated) {
			Log.trace("updating code "+code+" with attributes in "+updated);
			Attributes.mergeSystemAttributes(code.getAttributes(), updated.getAttributes());
			editorBus.fireEvent(new CodeUpdatedEvent(code));
		}
	}

}
