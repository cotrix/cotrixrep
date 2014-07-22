package org.cotrix.application.changelog;

import static org.cotrix.common.CommonUtils.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.cotrix.domain.managed.ManagedCode;

public abstract class CodelistChange {
	
	private final String id;
	private final String date;
	private final String user;
	
	
	CodelistChange(ManagedCode code) {
		
		this(code,code.lastUpdated());
		
	}

	CodelistChange(ManagedCode code, Date date) {
		
		notNull("date", date);
		
		this.id=code.managed().id();
		this.date= SimpleDateFormat.getDateTimeInstance().format(date);
		this.user = code.lastUpdatedBy();
	}
	
	public String id() {
		return id;
	}
	
	public String date() {
		return date;
	}
	
	public String user() {
		return user;
	}
	
	
	
	public static class NewCode extends CodelistChange {
	
		NewCode(ManagedCode code) {
			
			super(code,code.created());
		}
		
	}
	
	public static class DeletedCode extends CodelistChange {
		
		DeletedCode(ManagedCode code) {
			super(code);
		}
	}
	
	public static class ModifiedCode extends CodelistChange {
		
		private final List<CodeChange> changes;
		
		ModifiedCode(ManagedCode code,List<CodeChange> changes) {
			super(code);
			this.changes = changes;
		}
		
		public List<CodeChange> changes() {
			return changes;
		}
	
	}
	
	
	
}
