/**
 * 
 */
package org.cotrix.web.common.client.feature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class FeatureToggler implements HasFeature {

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setFeature() {
		toggleFeature(true);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void unsetFeature() {
		toggleFeature(false);
	}
	
	public abstract void toggleFeature(boolean active);

}
