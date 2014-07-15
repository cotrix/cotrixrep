/**
 * 
 */
package org.cotrix.web.common.shared.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractFeatureCarrier implements FeatureCarrier {
	
	public static class Void extends AbstractFeatureCarrier {
		
	}
	
	public static Void getVoid() {
		return new Void();
	}
	
	protected Set<UIFeature> applicationFeatures;
	protected Map<String, Set<UIFeature>> instancesFeatures;
	
	public AbstractFeatureCarrier(){}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Set<UIFeature> getApplicationFeatures() {
		return applicationFeatures;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationFeatures(Set<UIFeature> applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Set<UIFeature>> getInstancesFeatures() {
		return instancesFeatures;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void addInstancesFeatures(String instanceId, Set<UIFeature> instanceFeatures) {
		if (instancesFeatures == null) instancesFeatures = new HashMap<String, Set<UIFeature>>();
		instancesFeatures.put(instanceId, instanceFeatures);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Response [applicationFeatures=");
		builder.append(applicationFeatures);
		builder.append(", instancesFeatures=");
		builder.append(instancesFeatures);
		builder.append("]");
		return builder.toString();
	}	
}
