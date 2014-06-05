/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.view.client.HasRows;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PageSizer extends Composite {
	
	public interface PagerSizerResource extends ClientBundle {
		@Source("PageSizer.css")
		PageSizerStyle style();
	}
	
	public interface PageSizerStyle extends CssResource {
		String button();
		String selectedButton();
	}
	
	 private static PagerSizerResource DEFAULT_RESOURCES;

	  private static PagerSizerResource getDefaultResources() {
	    if (DEFAULT_RESOURCES == null) {
	      DEFAULT_RESOURCES = GWT.create(PagerSizerResource.class);
	    }
	    return DEFAULT_RESOURCES;
	  }
	
	private PageSizerStyle style;
	private InlineLabel[] sizeButtons;
	private HasRows hasRows;
	private int defaultSize;
	private InlineLabel defaultButton;
	
	@UiConstructor
	public PageSizer() {
		this(new int[]{25,50,100}, 25, getDefaultResources());
	}
	
	public PageSizer(int[] sizes, int defaultSize) {
		this(sizes, defaultSize, getDefaultResources());
	}
	
	public PageSizer(int[] sizes, int defaultSize, PagerSizerResource resource) {
		
		this.style = resource.style();
		this.style.ensureInjected();
		this.defaultSize = defaultSize;
		
		HorizontalPanel layout = new HorizontalPanel();
		
		sizeButtons = new InlineLabel[sizes.length];
		for (int i = 0; i < sizes.length; i++) {
			
			final int size = sizes[i];
			
			String label = String.valueOf(size);
			final InlineLabel button = new InlineLabel(label);
			button.setStyleName(style.button());
			button.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					onSizeChange(size);
					selectButton(button);
				}
			});
			button.setTitle("Shows "+size+" rows per page");
			
			layout.add(button);
			button.getElement().getParentElement().getStyle().setPadding(5, Unit.PX);
			button.getElement().getParentElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
			
			sizeButtons[i] = button;
			
			if (defaultSize == size) defaultButton = button;
		}
		
		if (defaultButton == null) throw new IllegalArgumentException("The specified default size "+defaultSize+" is not in the sizes list");
		
		initWidget(layout);
	}
	
	private void onSizeChange(int size) {
		if (hasRows!=null) {
			hasRows.setVisibleRange(hasRows.getVisibleRange().getStart(), size);
		}
	}
	
	private void selectButton(InlineLabel selectedButton) {
		for (InlineLabel button:sizeButtons) {
			button.setStyleName(style.selectedButton(), button == selectedButton);
		}
	}

	public void setDisplay(HasRows hasRows) {
		this.hasRows = hasRows;
		if (hasRows!=null) {
			onSizeChange(defaultSize);
			selectButton(defaultButton);
		}
	}
}
