package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

class ContextMenu extends Composite implements ContextMenuHandler {
  
  // just an example, use a meaningful Widget here...
  private Widget base;
 
  private PopupPanel contextMenu;
 
 
  public ContextMenu() {
    // initialize base widget, etc...
 
    this.contextMenu = new PopupPanel(true);
    this.contextMenu.add(new HTML("My Context menu!"));
    this.contextMenu.hide();
 
    initWidget(this.base);
 
    // of course it would be better if base would implement HasContextMenuHandlers, but the effect is the same
    addDomHandler(this, ContextMenuEvent.getType());
  }
 
 
  public void onContextMenu(ContextMenuEvent event) {
    // stop the browser from opening the context menu
    event.preventDefault();
    event.stopPropagation();
 
 
    this.contextMenu.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
    this.contextMenu.show();
  }
 
}