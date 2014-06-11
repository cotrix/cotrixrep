/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.FadeAnimation;
import org.cotrix.web.common.client.util.FadeAnimation.Speed;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.AdvancedSuggestBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SuggestListBox extends Composite implements HasValueChangeHandlers<String>, HasSelectionHandlers<SuggestOracle.Suggestion>, TakesValue<String> {
	
	private FlowPanel mainPanel;
	private HorizontalPanel layoutPanel;
	private AdvancedSuggestBox suggestBox;
	private PushButton suggestButton;
	private FadeAnimation buttonAnimation;
	
	public SuggestListBox(SuggestOracle oracle) {
		mainPanel = new FlowPanel();
		mainPanel.setStyleName(CotrixManagerResources.INSTANCE.detailsPanelStyle().suggestionbox());
		
		layoutPanel = new HorizontalPanel();
		layoutPanel.setWidth("100%");
		mainPanel.add(layoutPanel);

		suggestBox = new AdvancedSuggestBox(new SuggestOracleProxy(oracle), CotrixManagerResources.INSTANCE.detailsPanelStyle().suggestionItem(),
				CotrixManagerResources.INSTANCE.detailsPanelStyle().suggestionItemSelected());
		suggestBox.setWidth("100%");
		//suggestBox.setStyleName(CommonResources.INSTANCE.css().sugestionListBoxTextBox());
		suggestBox.getValueBox().setStyleName(CotrixManagerResources.INSTANCE.detailsPanelStyle().suggestionboxTextbox());
		suggestBox.setDisplayPopupStyleName(CotrixManagerResources.INSTANCE.detailsPanelStyle().suggestionPopup());
		layoutPanel.add(suggestBox);
		
		suggestButton = new PushButton(new Image(CommonResources.INSTANCE.selectArrow()));
		suggestButton.setStyleName(CotrixManagerResources.INSTANCE.detailsPanelStyle().suggestionboxButton());
		buttonAnimation = new FadeAnimation(suggestButton.getElement());
		buttonAnimation.fadeOut(Speed.IMMEDIATE);
		mainPanel.addDomHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (suggestBox.isEnabled()) buttonAnimation.fadeIn(Speed.VERY_FAST);
			}
		}, MouseOverEvent.getType());
		
		mainPanel.addDomHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (suggestBox.isEnabled()) buttonAnimation.fadeOut(Speed.VERY_FAST);
			}
		}, MouseOutEvent.getType());
		
		layoutPanel.add(suggestButton);
		layoutPanel.setCellWidth(suggestButton, "15px");
		
		suggestButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.trace("show suggestions");
				suggestBox.showSuggestions("");
				
			}
		});
		initWidget(mainPanel);
	}
	
	private class SuggestOracleProxy extends SuggestOracle {
		
		private SuggestOracle proxed;

		/**
		 * @param proxed
		 */
		public SuggestOracleProxy(SuggestOracle proxed) {
			this.proxed = proxed;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void requestDefaultSuggestions(Request request, Callback callback) {
			requestSuggestions(new Request("", request.getLimit()), callback);
		}

		@Override
		public void requestSuggestions(Request request, Callback callback) {
			proxed.requestSuggestions(request, callback);
		}
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return suggestBox.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<Suggestion> handler) {
		return suggestBox.addSelectionHandler(handler);
	}

	public ValueBoxBase<String> getValueBox() {
		return suggestBox.getValueBox();
	}

	@Override
	public void setValue(String value) {
		suggestBox.setValue(value);
	}

	@Override
	public String getValue() {
		return suggestBox.getValue();
	}

	public void setEnabled(boolean enabled) {
		mainPanel.setStyleName(CotrixManagerResources.INSTANCE.detailsPanelStyle().suggestionbox(), enabled);
		suggestBox.setEnabled(enabled);
	}
}
