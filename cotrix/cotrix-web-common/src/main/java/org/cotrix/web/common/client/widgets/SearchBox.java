/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import org.cotrix.web.common.client.resources.CommonResources;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SearchBox extends Composite implements HasValueChangeHandlers<String> {
	
	private TextBox textBox;
	private Timer changeTimer;
	private int changeDelay = 0;
	
	public SearchBox() {
		textBox = new TextBox();
		textBox.setStylePrimaryName(CommonResources.INSTANCE.css().searchBox());
		textBox.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				updateBoxStyle();
				changeTimer.schedule(changeDelay);	
			}
		});
		
		 changeTimer = new Timer() {
				
				@Override
				public void run() {
					fireUpdate();
				}
			};
		
		updateBoxStyle();
		initWidget(textBox);
	}
	
	public String getValue() {
		return textBox.getValue();
	}
	
	public void setChangeDelay(int millis) {
		this.changeDelay = millis;
	}
	
	public void clear() {
		textBox.setValue("", false);
		updateBoxStyle();
	}
	
	private void fireUpdate() {
		ValueChangeEvent.fire(SearchBox.this, textBox.getValue());
	}
	
	private void updateBoxStyle() {
		textBox.setStyleName(CommonResources.INSTANCE.css().searchBackground(), textBox.getValue().isEmpty());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void setFocus(boolean focused) {
		textBox.setFocus(focused);
	}

}
