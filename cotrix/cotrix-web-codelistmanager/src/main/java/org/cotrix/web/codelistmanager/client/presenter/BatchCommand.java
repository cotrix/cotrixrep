package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.shared.CodeCell;

public class BatchCommand {
	private String command;
	private String value;
	private CodeCell codeCell;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public CodeCell getCodeCell() {
		return codeCell;
	}
	public void setCodeCell(CodeCell codeCell) {
		this.codeCell = codeCell;
	}
	
}
