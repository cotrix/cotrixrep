package org.cotrix.application.validation;

import org.cotrix.common.Report;

public class ValidationItem extends Report.Item {

	private final String id;
	private final String msg;
	
	public static final ValidationItem item(String id,String msg) {
		return new ValidationItem(id, msg);
	}
	
	public ValidationItem(String id,String msg) {
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
