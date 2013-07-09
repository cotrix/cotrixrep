/**
 * 
 */
package org.cotrix.web.importwizard.client.wizard;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface NavigationButtonConfiguration {
	
	public enum Type {BACKWARD, FORWARD};
	
	public String getId();
	
	public String getLabel();
	
	public Type getType();

}
