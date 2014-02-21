/**
 * 
 */
package org.cotrix.web.share.client.feature;

import org.cotrix.web.share.client.widgets.HasEditing;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class HasEditableFeature implements HasFeature {
	
	protected HasEditing hasEditing;

	/**
	 * @param hasEditing
	 */
	public HasEditableFeature(HasEditing hasEditing) {
		this.hasEditing = hasEditing;
	}

	@Override
	public void setFeature() {
		hasEditing.setEditable(true);
	}

	@Override
	public void unsetFeature() {
		hasEditing.setEditable(false);
	}
}
