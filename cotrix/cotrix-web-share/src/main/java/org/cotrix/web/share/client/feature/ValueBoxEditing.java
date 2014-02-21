/**
 * 
 */
package org.cotrix.web.share.client.feature;

import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ValueBoxEditing<T> extends FeatureToggler {
	
	protected ValueBoxBase<T> valueBox;

	/**
	 * @param valueBox
	 */
	public ValueBoxEditing(ValueBoxBase<T> valueBox) {
		this.valueBox = valueBox;
	}

	@Override
	public void toggleFeature(boolean active) {
		valueBox.setReadOnly(!active);		
	}

}
