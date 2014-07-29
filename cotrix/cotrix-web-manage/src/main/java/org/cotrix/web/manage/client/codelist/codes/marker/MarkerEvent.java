/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

import com.google.gwt.resources.client.ImageResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerEvent {
	
	private ImageResource icon;
	private String title;
	private String subTitle;
	private String description;
	private String timestamp;

	public ImageResource getIcon() {
		return icon;
	}

	public void setIcon(ImageResource icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MarkerEvent [icon=");
		builder.append(icon);
		builder.append(", title=");
		builder.append(title);
		builder.append(", subTitle=");
		builder.append(subTitle);
		builder.append(", description=");
		builder.append(description);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append("]");
		return builder.toString();
	}
}
