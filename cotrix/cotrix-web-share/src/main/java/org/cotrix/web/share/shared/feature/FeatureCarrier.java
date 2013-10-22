/**
 * 
 */
package org.cotrix.web.share.shared.feature;

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

	/**
	 * @param codelistsFeatures the codelistsFeatures to set
	 */
	public void setCodelistsFeatures(Map<String, Set<UIFeature>> codelistsFeatures) {
		this.codelistsFeatures = codelistsFeatures;
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
