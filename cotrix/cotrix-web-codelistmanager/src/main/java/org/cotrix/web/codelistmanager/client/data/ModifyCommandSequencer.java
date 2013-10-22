/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import java.util.LinkedList;
import java.util.Queue;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.codelist.CodelistId;
import org.cotrix.web.codelistmanager.shared.modify.ContainsIdentifiable;
import org.cotrix.web.codelistmanager.shared.modify.GeneratedId;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ModifyCommandSequencer {
	
	protected static final int MAX_ATTEMPTS = 3;
	
	@Inject
	protected ManagerServiceAsync service;
	
	@Inject
	@CodelistId
	protected String codelistId;
	
	protected Queue<Task> tasks = new LinkedList<Task>();
	protected int taskAttempts = 0;
	protected boolean processing = false;
	
	public void enqueueCommand(ModifyCommand command, Callback<Void, Throwable> callback)
	{
		Task task = new Task(command, callback);
		tasks.add(task);
		if (!processing) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				
				@Override
				public void execute() {
					start();
				}
			});
			
		}
	}
	
	protected void execute(final Task task)
	{
		taskAttempts++;
		ModifyCommand command = task.getCommand();
		Log.trace("executing command "+command+" attempt "+taskAttempts);
		service.modify(codelistId, command, new AsyncCallback<ModifyCommandResult>() {
			
			@Override
			public void onSuccess(ModifyCommandResult result) {
				commandSuccessfullyExecuted(task, result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				commandFailed(task, caught);
			}
		});
	}
	
	protected void executeNext()
	{
		if (tasks.size() == 0) {
			stop();
			return;
		}
		
		Task task = tasks.remove();
		taskAttempts = 0;
		execute(task);
	}
	
	protected void stop()
	{
		processing = false;
	}
	
	protected void start()
	{
		processing = true;
		executeNext();
	}
	
	protected void commandSuccessfullyExecuted(Task task, ModifyCommandResult result)
	{
		Log.trace("commandSuccessfullyExecuted result: "+result);
		
		if (task.getCommand() instanceof ContainsIdentifiable && result instanceof GeneratedId) {
			Log.trace("setting id in identifiable object");
			ContainsIdentifiable containsIdentifiable = (ContainsIdentifiable) task.getCommand();
			GeneratedId generatedId = (GeneratedId) result;
			containsIdentifiable.getIdentifiable().setId(generatedId.getId());
		}
		
		task.getCallback().onSuccess(null);
		executeNext();
	}
	
	protected void commandFailed(Task task, Throwable caught)
	{
		Log.warn("command failed: "+task.getCommand(), caught);
		if (taskAttempts>MAX_ATTEMPTS) {
			Log.error("Commands attempts limit reached, skipping it");
			executeNext();
		} else {
			execute(task);
		}
	}
	
	protected class Task {
		protected ModifyCommand command;
		protected Callback<Void, Throwable> callback;
		
		/**
		 * @param command
		 * @param callback
		 */
		public Task(ModifyCommand command, Callback<Void, Throwable> callback) {
			this.command = command;
			this.callback = callback;
		}

		/**
		 * @return the command
		 */
		public ModifyCommand getCommand() {
			return command;
		}

		/**
		 * @return the callback
		 */
		public Callback<Void, Throwable> getCallback() {
			return callback;
		}
		
	}

}
