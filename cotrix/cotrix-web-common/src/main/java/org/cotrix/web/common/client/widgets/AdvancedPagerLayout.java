/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AdvancedPagerLayout extends ResizeComposite {
	
	private LayoutPanel layoutPanel;
	private FlowPanel pagerContainer;
	private PageSizer pageSizer;
	
	public AdvancedPagerLayout() {
		layoutPanel = new LayoutPanel();
		initWidget(layoutPanel);
	}
	
	@UiChild
	public void addPager(SimplePager pager) {
		if (pagerContainer == null) {
			pagerContainer = new FlowPanel();
			layoutPanel.add(pagerContainer);
			layoutPanel.setWidgetTopHeight(pagerContainer, 0, Unit.PX, 30, Unit.PX);
		}
		
		pagerContainer.clear();
		pagerContainer.add(pager);
		pager.getElement().getStyle().setProperty("margin", "0 auto");
	}
	
	@UiChild
	public void addPageSizer(PageSizer pageSizer) {
		if (this.pageSizer!=null) layoutPanel.remove(this.pageSizer);
		this.pageSizer = pageSizer;
		layoutPanel.add(pageSizer);
		layoutPanel.setWidgetRightWidth(pageSizer, 0, Unit.PX, 100, Unit.PX);
	}

}
