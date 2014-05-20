/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import org.cotrix.web.common.client.widgets.EnumListBox.LabelProvider;
import org.cotrix.web.common.shared.Language;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LanguageListBox extends Composite implements HasValueChangeHandlers<Language>, TakesValue<Language> {
	
	private static final LabelProvider<Language> LABEL_PROVIDER = new LabelProvider<Language>() {
		public String getLabel(Language item) {
			return item.getName();
		}
	};
	
	private EnumListBox<Language> languagesBox;
	
	public LanguageListBox() {
		
		languagesBox = new EnumListBox<Language>(Language.class, LABEL_PROVIDER);
		
		languagesBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				ValueChangeEvent.fire(LanguageListBox.this, getValue());
			}
		});
		
		initWidget(languagesBox);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Language> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void setEnabled(boolean enabled) {
		languagesBox.setEnabled(enabled);
	}

	@Override
	public void setValue(Language language) {
		languagesBox.setSelectedValue(language);
	}

	@Override
	public Language getValue() {
		return languagesBox.getSelectedValue();
	}

}
