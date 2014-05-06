/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import java.util.Arrays;

import org.cotrix.web.common.client.resources.CommonConstants;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LanguageListBox extends Composite implements HasValueChangeHandlers<String> {
	
	private static final String[] LANGUAGES = CommonConstants.INSTANCE.languages();
	static {
		Arrays.sort(LANGUAGES);
	}
	
	private static final String NO_LANGUAGE_VALUE = "NO_LANGUAGE";
	
	private ListBox languagesBox;
	
	public LanguageListBox() {
		languagesBox = new ListBox();
		setValues();
		
		languagesBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				ValueChangeEvent.fire(LanguageListBox.this, getLanguage());
			}
		});
		
		initWidget(languagesBox);
	}
	
	private void setValues() {
		languagesBox.addItem(" ", NO_LANGUAGE_VALUE);
		for (String language:LANGUAGES) languagesBox.addItem(language);
	}
	
	public String getLanguage() {
		int selectedIndex = languagesBox.getSelectedIndex();
		if (selectedIndex<0) return null;
		String value = languagesBox.getValue(selectedIndex);
		return NO_LANGUAGE_VALUE.equals(value)?null:value;
	}
	
	public void setLanguage(String language) {
		if (language == null) {
			selectValue(NO_LANGUAGE_VALUE);
		} else {
			boolean selected = selectValue(language);
			if (!selected) throw new IllegalArgumentException("Invalid language "+language+" available values: "+Arrays.toString(LANGUAGES));
		}
	}
	
	private boolean selectValue(String value) {
		for (int i = 0; i < languagesBox.getItemCount(); i++) {
			if (languagesBox.getItemText(i).equals(value)) {
				languagesBox.setSelectedIndex(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void setEnabled(boolean enabled) {
		languagesBox.setEnabled(enabled);
	}

}
