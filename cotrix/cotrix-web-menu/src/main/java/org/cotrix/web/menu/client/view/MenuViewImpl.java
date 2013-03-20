package org.cotrix.web.menu.client.view;

import org.cotrix.web.menu.client.presenter.MenuPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MenuViewImpl extends Composite implements MenuView {

	@UiTemplate("MenuView.ui.xml")
	interface MenuViewUiBinder extends UiBinder<Widget, MenuViewImpl> {}
	private static MenuViewUiBinder uiBinder = GWT.create(MenuViewUiBinder.class);

	@UiField FlowPanel menubar;
	@UiField Style style;

	interface Style extends CssResource {
		String selectedLabel();
		String label();
	}
	
	private Presenter presenter;
	
	public MenuViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void showMenu() {
		for (int i = 0; i < menubar.getWidgetCount(); i++) {
			Label label = (Label) menubar.getWidget(i);
			final int index = i;
			label.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					setSelectedMenu(index);
				}
			});
		}
	}
	
	private void setSelectedMenu(int index){
		for (int i = 0; i < menubar.getWidgetCount(); i++) {
			Label label = (Label) menubar.getWidget(i);
			if(index == i){
				label.setStyleName(style.selectedLabel());
			}else{
				label.setStyleName(style.label());
			}
		}
	}
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
