package org.cotrix.application.changelog;

import static org.cotrix.common.CommonUtils.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.cotrix.application.managed.ManagedCode;

public abstract class CodeChange {
	
	private final String id;
	private final String date;
	private final String user;
	
	CodeChange(ManagedCode code, Date date) {
		
		notNull("date", date);
		
		System.out.println(code.managed());
		
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
	
	
	
	public static class New extends CodeChange {
	
		New(ManagedCode code) {
			
			super(code,code.created());
		}
		
	}
	
	public static class Deleted extends CodeChange {
		
		Deleted(ManagedCode code) {
			super(code,code.lastUpdated());
		}
	}
	
	public static class Modified extends CodeChange {
		Modified(ManagedCode code) {
			super(code,code.lastUpdated());
		}
	}
	
	
	
}
