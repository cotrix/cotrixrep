/**
 * 
 */
package org.cotrix.web.client.main;

import org.cotrix.web.common.client.widgets.HasMinHeight;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Container extends Composite implements RequiresResize {
	
	private FlowPanel container;
	private DeckLayoutPanel deck;
	private Widget footer;
	
	public Container() {
		container = new FlowPanel();
		initWidget(container);
	}
	
	@UiChild
	public void addDeck(DeckLayoutPanel deck) {
		this.deck = deck;
		container.add(deck);
	}
	
	@UiChild
	public void addFooter(Widget footer) {
		this.footer = footer;
		container.add(footer);
	}
	
	public void showWidget(int index) {
		deck.showWidget(index);
		Widget widget = deck.getWidget(index);
		String newHeight = (widget instanceof HasMinHeight)?((HasMinHeight)widget).getMinHeight()+"px":widget.getElement().getStyle().getHeight();
		deck.setHeight(newHeight);
		
		int fillingSpace = container.getOffsetHeight() - footer.getOffsetHeight();
		System.out.println("filling space: "+fillingSpace);
		System.out.println("deck h: "+deck.getOffsetHeight());
		System.out.println("deck children h: "+deck.getVisibleWidget().getOffsetHeight());
		if (deck.getOffsetHeight()<fillingSpace) {
			System.out.println("new height: "+fillingSpace);
			deck.setHeight(fillingSpace+"px");
		}
	}
	
	@Override
	public void onResize() {
		System.out.println();
		System.out.println("onResize");
		System.out.println("composite h: "+getOffsetHeight());
		System.out.println("container h: "+container.getOffsetHeight());
		System.out.println("deck h: "+deck.getOffsetHeight());
		System.out.println("deck children h: "+deck.getVisibleWidget().getOffsetHeight());
		System.out.println("footer h: "+footer.getOffsetHeight());
		
		int fillingSpace = container.getOffsetHeight() - footer.getOffsetHeight();
		
		Widget widget = deck.getVisibleWidget();
		String newHeight = (widget instanceof HasMinHeight)?((HasMinHeight)widget).getMinHeight()+"px":widget.getElement().getStyle().getHeight();
		deck.setHeight(newHeight);
		
		System.out.println("filling space: "+fillingSpace);
		if (deck.getOffsetHeight()<fillingSpace) {
			System.out.println("new height: "+fillingSpace);
			
			deck.setHeight(fillingSpace+"px");
			
			if (widget instanceof RequiresResize) ((RequiresResize)widget).onResize();
		}
		
	}
}
