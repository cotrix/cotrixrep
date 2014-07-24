/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AutoHeightTextArea extends Composite {
	
	private TextArea area;
	private Element measuringElement;
	
	public AutoHeightTextArea() {
		area = new TextArea();
		area.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String text = area.getValue();
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) text +=" ";
				updateHeight(text);
			}
		});
		createMeasuringElement();
		initWidget(area);
		updateHeight("a");
	}
	
	private void updateHeight(String text) {
		if (text == null) text = "a";
		if (text.isEmpty()) text+="a";
		
		int width = area.getElement().getOffsetWidth() - 6;
		double height  = measureTextHeight(width, text);

		//we add textarea padding
		height += 6;
		
		System.out.println("size: "+height + " OffsetWidth: "+width+ " ClientWidth: "+ area.getElement().getClientWidth()+" value.length(): "+area.getValue().length());
		area.getElement().getStyle().setHeight(height, Unit.PX);
	}
	
	private void createMeasuringElement() {
		Document document = Document.get();
		measuringElement = document.createElement("div");
		measuringElement.getStyle().setPosition(Position.ABSOLUTE);
		measuringElement.getStyle().setLeft(-1000, Unit.PX);
		measuringElement.getStyle().setTop(-1000, Unit.PX);
		/*measuringElement.getStyle().setProperty("border", "1px solid black");
		measuringElement.getStyle().setLeft(500, Unit.PX);
		measuringElement.getStyle().setTop(500, Unit.PX);*/
		measuringElement.getStyle().setProperty("wordWrap", "break-word");
		measuringElement.getStyle().setProperty("whiteSpace", "pre-wrap");
		measuringElement.getStyle().setLineHeight(20, Unit.PX);
		document.getBody().appendChild(measuringElement);
	}
	
	private double measureTextHeight(double width, String text) {
		measuringElement.getStyle().setWidth(width, Unit.PX);
		measuringElement.setInnerHTML(text);
		return measuringElement.getClientHeight();
	}

	@Override
	public void addStyleName(String style) {
		measuringElement.setClassName(style);
		super.addStyleName(style);
	}

	@Override
	public void setStyleName(String style) {
		measuringElement.setClassName(style);
		super.setStyleName(style);
	}

	public String getValue() {
		return area.getValue();
	}

	public void setValue(String value) {
		area.setValue(value);
		updateHeight(value);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return area.addValueChangeHandler(handler);
	}

	public void setEnabled(boolean enabled) {
		area.setEnabled(enabled);
	}

	public void setFocus(boolean focused) {
		area.setFocus(focused);
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return area.addKeyDownHandler(handler);
	}

	@Override
	public void setVisible(boolean visible) {
		System.out.println("setVisible visible: "+visible);
		super.setVisible(visible);
		updateHeight(area.getValue());
	}
	
	
	public void setPlaceholder(String placeholder)
    {
       area.getElement().setAttribute("placeholder", placeholder);
    }
}
