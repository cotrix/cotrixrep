/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.event;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkerEvent {
	public enum Type {ERROR,ADDITION,DELETION,CHANGE}
	
	Type getType();
	
	String getTitle();

	String getSubtitle();

	String getDescription();

	String getTimestamp();

	String getUser();
}
