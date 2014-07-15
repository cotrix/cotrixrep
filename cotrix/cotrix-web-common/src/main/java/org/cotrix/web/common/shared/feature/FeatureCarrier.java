package org.cotrix.web.common.shared.feature;

import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface FeatureCarrier extends IsSerializable {

	/**
	 * @return the features
	 */
	public Set<UIFeature> getApplicationFeatures();

	/**
	 * @param applicationFeatures the features to set
	 */
	public void setApplicationFeatures(Set<UIFeature> applicationFeatures);

	/**
	 * @return the codelistsFeatures
	 */
	public Map<String, Set<UIFeature>> getInstancesFeatures();

	public void addInstancesFeatures(String instanceId,
			Set<UIFeature> instanceFeatures);

}