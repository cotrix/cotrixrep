/**
 * 
 */
package org.cotrix.web.common.shared;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.common.shared.async.AsyncOutcome;
import org.cotrix.web.common.shared.feature.FeatureCarrier;
import org.cotrix.web.common.shared.feature.UIFeature;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LongTaskProgress extends Progress implements FeatureCarrier {
	
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
	public Set<UIFeature> getApplicationFeatures() {
		return (outcome!=null)?outcome.getOutcome().getApplicationFeatures():Collections.<UIFeature>emptySet();
	}


	@Override
	public void setApplicationFeatures(Set<UIFeature> applicationFeatures) {
		outcome.getOutcome().setApplicationFeatures(applicationFeatures);
	}


	@Override
	public Map<String, Set<UIFeature>> getInstancesFeatures() {
		return (outcome!=null)?outcome.getOutcome().getInstancesFeatures():Collections.<String, Set<UIFeature>>emptyMap();
	}


	@Override
	public void addInstancesFeatures(String instanceId, Set<UIFeature> instanceFeatures) {
		outcome.getOutcome().addInstancesFeatures(instanceId, instanceFeatures);
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
		builder.append(", outcome=");
		builder.append(outcome);
		builder.append("]");
		return builder.toString();
	}

}
