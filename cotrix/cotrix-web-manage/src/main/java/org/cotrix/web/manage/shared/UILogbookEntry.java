/**
 * 
 */
package org.cotrix.web.manage.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UILogbookEntry implements IsSerializable {
	
	public enum UILogbookEvent {

		CREATED,
		IMPORTED,
		PUBLISHED,
		VERSIONED,
		SEALED,
		LOCKED;
	}
	
	private String id;
	private String timestamp;
	private UILogbookEvent event;
	private String description;
	private String user;
	private boolean removable;
	
	public UILogbookEntry() {}
	
	public UILogbookEntry(String id, String timestamp, UILogbookEvent event,
			String description, String user, boolean removable) {
		this.id = id;
		this.timestamp = timestamp;
		this.event = event;
		this.description = description;
		this.user = user;
		this.removable = removable;
	}

	public String getId() {
		return id;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public UILogbookEvent getEvent() {
		return event;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getUser() {
		return user;
	}
	
	public boolean isRemovable() {
		return removable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UILogbookEntry [id=");
		builder.append(id);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", event=");
		builder.append(event);
		builder.append(", description=");
		builder.append(description);
		builder.append(", user=");
		builder.append(user);
		builder.append(", removable=");
		builder.append(removable);
		builder.append("]");
		return builder.toString();
	}
}
