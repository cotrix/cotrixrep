/**
 * 
 */
package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ContentPanel extends ResizeComposite {
	
	@UiTemplate("ContentPanel.ui.xml")
	interface ContentPanelUiBinder extends UiBinder<Widget, ContentPanel> {
	}

	private static ContentPanelUiBinder uiBinder = GWT.create(ContentPanelUiBinder.class);
	
	@UiField DeckLayoutPanel contenPanel;
	@UiField TabLayoutPanel codelistsPanel;
	@UiField HTMLPanel blankPanel;
	
	public ContentPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		codelistsPanel.setAnimationDuration(0);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void showBlank()
	{
		contenPanel.showWidget(blankPanel);
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
	public void addCodeListPanel(Widget panel, String title)
	{
		codelistsPanel.add(panel, title);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void removeCodeListPanel(Widget panel)
	{
		codelistsPanel.remove(panel);
	}

}
