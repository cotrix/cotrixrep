/**
 * 
 */
package org.cotrix.web.common.shared.async;

import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AsyncOutcome<T extends IsSerializable> extends AbstractFeatureCarrier implements AsyncOutput<T> {
	
	public static class Void implements IsSerializable {
	}

	private T outcome;

	protected AsyncOutcome(){}

	
	public AsyncOutcome(T outcome) {
		this.outcome = outcome;
	}
	
	public T getOutcome() {
		return outcome;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AsyncOutcome [outcome=");
		builder.append(outcome);
		builder.append("]");
		return builder.toString();
	}
}
