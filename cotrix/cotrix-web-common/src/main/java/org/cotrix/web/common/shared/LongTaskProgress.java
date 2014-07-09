/**
 * 
 */
package org.cotrix.web.common.shared;

import org.cotrix.web.common.shared.async.AsyncOutcome;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LongTaskProgress extends Progress {
	
	private int percentage;
	private String message;
	private AsyncOutcome<? extends IsSerializable> outcome;
	
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

	public AsyncOutcome<?> getOutcome() {
		return outcome;
	}

	public void setOutcome(AsyncOutcome<? extends IsSerializable> outcome) {
		this.outcome = outcome;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LongTaskProgress [percentage=");
		builder.append(percentage);
		builder.append(", message=");
		builder.append(message);
		builder.append(", outcome=");
		builder.append(outcome);
		builder.append("]");
		return builder.toString();
	}
}
