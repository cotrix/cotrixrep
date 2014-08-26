/**
 * 
 */
package org.cotrix.web.manage.client.manager;

import org.cotrix.web.manage.client.util.EmptyPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ContentPanel extends ResizeComposite {
	
	public interface ContentPanelListener {
		public void onCodelistTabSelected(Widget codelistPanel);
	}
	
	@UiTemplate("ContentPanel.ui.xml")
	interface ContentPanelUiBinder extends UiBinder<Widget, ContentPanel> {
	}

	private static ContentPanelUiBinder uiBinder = GWT.create(ContentPanelUiBinder.class);
	
	@UiField DeckLayoutPanel contenPanel;
	@UiField TabLayoutPanel codelistsPanel;
	@UiField EmptyPanel emptyPanel;
	
	public ContentPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		codelistsPanel.setAnimationDuration(0);
		emptyPanel.setMessage("no codelist selected.");
	}
	
	public void addListener(final ContentPanelListener listener) {
		codelistsPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				Integer index = event.getSelectedItem();
				if (index!=null) {
					Widget codelistWidget = codelistsPanel.getWidget(index);
					listener.onCodelistTabSelected(codelistWidget);
				}
			}
		});
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void showEmpty()
	{
		contenPanel.showWidget(emptyPanel);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void showCodelists()
	{
		contenPanel.showWidget(codelistsPanel);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public HasCloseHandlers<Widget> addCodeListPanel(Widget codelistPanel, String title, String version)
	{
		CodelistTab tab = new CodelistTab(title, version);
		codelistsPanel.add(codelistPanel.asWidget(), tab);
		return tab;
	}
	
	public void setVisible(Widget codelistPanel)
	{
		codelistsPanel.selectTab(codelistPanel.asWidget());
	}

	/** 
	 * {@inheritDoc}
	 */
	public void removeCodeListPanel(Widget panel)
	{
		codelistsPanel.remove(panel.asWidget());
	}

}
