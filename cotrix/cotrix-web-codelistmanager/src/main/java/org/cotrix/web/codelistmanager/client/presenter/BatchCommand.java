package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.share.shared.UICode;


public class BatchCommand {
	private String command;
	private UICode code;
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public UICode getCode() {
		return code;
	}
	public void setCode(UICode code) {
		this.code = code;
	}
}
