/**
 * 
 */
package org.cotrix.web.common.client.feature;

import com.google.gwt.user.client.ui.HasVisibility;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class HasVisibleFeature implements HasFeature {
	
	protected HasVisibility hasVisibility;

	/**
	 * @param hasVisibility
	 */
	public HasVisibleFeature(HasVisibility hasVisibility) {
		this.hasVisibility = hasVisibility;
	}

	@Override
	public void setFeature() {
		hasVisibility.setVisible(true);
	}

	@Override
	public void unsetFeature() {
		hasVisibility.setVisible(false);
	}
}
