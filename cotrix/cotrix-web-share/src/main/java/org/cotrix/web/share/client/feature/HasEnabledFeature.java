/**
 * 
 */
package org.cotrix.web.share.client.feature;

import com.google.gwt.user.client.ui.HasEnabled;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class HasEnabledFeature implements HasFeature {
	
	protected HasEnabled hasEnabled;

	/**
	 * @param hasEnabled
	 */
	public HasEnabledFeature(HasEnabled hasEnabled) {
		this.hasEnabled = hasEnabled;
	}

	@Override
	public void setFeature() {
		hasEnabled.setEnabled(true);
	}

	@Override
	public void unsetFeature() {
		hasEnabled.setEnabled(false);
	}
}
