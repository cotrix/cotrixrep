package org.cotrix.web.common.client.widgets.cell;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EditableTextHeader extends Header<String> {

	private String headerValue = "";

	public EditableTextHeader(AbstractInputCell<String, ?> cell, String value) {
		super(cell);
		this.headerValue = value;
		setUpdater(new ValueUpdater<String>() {
			
			@Override
			public void update(String updatedValue) {
				headerValue = updatedValue;
			}
		});
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return headerValue;
	}
}
