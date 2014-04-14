package org.cotrix.common;

import org.cotrix.common.Report.Item;

public class Log extends Item {
	
	public static final Log item(String msg) {
		return new Log(msg);
	}
	
	final String msg;
	
	private Log(String msg) {
		this.msg=msg;
	}

	public String message() {
		return msg;
	}
}
