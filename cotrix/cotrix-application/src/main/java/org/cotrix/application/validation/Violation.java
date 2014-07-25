package org.cotrix.application.validation;

import org.cotrix.common.Report;

public class Violation extends Report.Item {

	private final String id;
	private final String msg;
	
	public static final Violation make(String id,String msg) {
		return new Violation(id, msg);
	}
	
	public Violation(String id,String msg) {
		this.id=id;
		this.msg = msg;
	}
	
	public String id() {
		return id;
	}
	
	public String message() {
		return msg;
	};
}
