/**
 * 
 */
package org.cotrix.web.share.shared.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class FeatureCarrier implements IsSerializable {
	
	public static class Void extends FeatureCarrier {
		
	}
	
	public static Void getVoid() {
		return new Void();
	}
	
	protected Set<UIFeature> applicationFeatures;
	protected Map<String, Set<UIFeature>> codelistsFeatures;
	
	public FeatureCarrier(){}

	/**
	 * @return the features
	 */
	public Set<UIFeature> getApplicationFeatures() {
		return applicationFeatures;
	}

	/**
	 * @param applicationFeatures the features to set
	 */
	public void setApplicationFeatures(Set<UIFeature> applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	/**
	 * @return the codelistsFeatures
	 */
	public Map<String, Set<UIFeature>> getCodelistsFeatures() {
		return codelistsFeatures;
	}

	public void addCodelistsFeatures(String codelistId, Set<UIFeature> codelistFeatures) {
		if (codelistsFeatures == null) codelistsFeatures = new HashMap<String, Set<UIFeature>>();
		codelistsFeatures.put(codelistId, codelistFeatures);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Response [applicationFeatures=");
		builder.append(applicationFeatures);
		builder.append(", codelistsFeatures=");
		builder.append(codelistsFeatures);
		builder.append("]");
		return builder.toString();
	}	
}
