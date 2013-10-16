/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class RemoveCommand implements ModifyCommand {
	
	protected String id;
	
	protected RemoveCommand(){}

	/**
	 * @param id
	 */
	public RemoveCommand(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
