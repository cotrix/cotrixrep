/**
 * 
 */
package org.cotrix.web.share.client.widgets;

import java.util.Iterator;

import org.cotrix.web.share.client.resources.CommonResources;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LoadingPanel extends ResizeComposite implements HasWidgets {
	
	protected DeckLayoutPanel container;
	protected HTMLPanel loader;
	protected Widget innerWidget;
	
	public LoadingPanel()
	{
		container = new DeckLayoutPanel();
		initWidget(container);
		setupLoadingContainer();
	}
	
	protected void setupLoadingContainer()
	{
		String id = Document.get().createUniqueId();
		String html =  "<table width=\"100%\" height=\"100%\"><tr><td style=\"text-align: center; vertical-align: middle;\">" +
				"<img id=\""+id+"\"/></td></tr></table>";
		
		loader = new HTMLPanel(html);
		Image loaderImage = new Image(CommonResources.INSTANCE.dataLoader());
		loader.addAndReplaceElement(loaderImage, id);

		container.add(loader);
	}
	
	public void showLoader()
	{
		container.showWidget(loader);
	}
	
	public void hideLoader()
	{
		if (innerWidget == null) throw new IllegalStateException("No innerwidget set");
		container.showWidget(innerWidget);
		//if (innerWidget instanceof RequiresResize) ((RequiresResize)innerWidget).onResize();
	}

	@Override
	public void add(Widget w) {
		container.clear();
		container.add(loader);
		container.add(w);
		innerWidget = w;
		container.showWidget(innerWidget);
	}

	@Override
	public void clear() {
		container.clear();
		container.add(loader);
		innerWidget = null;
	}

	@Override
	public Iterator<Widget> iterator() {
		return container.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		if (w == innerWidget) innerWidget = null;
		return container.remove(w);
	}

}
