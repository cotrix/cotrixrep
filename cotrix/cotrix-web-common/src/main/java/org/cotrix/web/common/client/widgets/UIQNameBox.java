/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIQNameBox extends Composite implements HasValue<UIQName> {
	
	private String localPart;
	private String namespace;
	private AdvancedTextBox textBox;
	
	public UIQNameBox() {
		textBox = new AdvancedTextBox();
		textBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fireValueChange();				
			}
		});
		initWidget(textBox);
	}
	
	private void fireValueChange() {
		ValueChangeEvent.fire(this, getValue());
	}
	
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<UIQName> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public UIQName getValue() {
		String value = textBox.getValue();
		//if the local part is changed we clean the namespace
		if (localPart != null && !localPart.equals(value)) {
			namespace = ValueUtils.defaultNamespace;
		}
		localPart = value;
		return new UIQName(namespace, value);
	}

	@Override
	public void setValue(UIQName value) {
		setValue(value, false);
	}

	@Override
	public void setValue(UIQName value, boolean fireEvents) {
		namespace = value!=null?value.getNamespace():null;
		localPart = value!=null?value.getLocalPart():null;
		textBox.setValue(localPart, fireEvents);
	}

	public void setPlaceholder(String placeholder) {
		textBox.setPlaceholder(placeholder);
	}

	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return textBox.addKeyUpHandler(handler);
	}

	public void setEnabled(boolean enabled) {
		textBox.setEnabled(enabled);
	}

	public void setFocus(boolean focused) {
		textBox.setFocus(focused);
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return textBox.addKeyDownHandler(handler);
	}
}
