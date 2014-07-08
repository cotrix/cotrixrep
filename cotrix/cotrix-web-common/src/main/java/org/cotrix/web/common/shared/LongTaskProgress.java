/**
 * 
 */
package org.cotrix.web.common.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LongTaskProgress extends Progress {
	
	private int percentage;
	private String message;
	
	public int getPercentage() {
		return percentage;
	}
	
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LongTaskProgress [status=");
		builder.append(status);
		builder.append(", failureCause=");
		builder.append(failureCause);
		builder.append(", percentage=");
		builder.append(percentage);
		builder.append(", message=");
		builder.append(message);
		builder.append("]");
		return builder.toString();
	}
}
