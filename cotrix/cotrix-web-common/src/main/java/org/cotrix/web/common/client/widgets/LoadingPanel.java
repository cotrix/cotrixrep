/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import java.util.Iterator;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.FadeAnimation;
import org.cotrix.web.common.client.util.FadeAnimation.Speed;

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
	protected boolean isAnimated;
	protected FadeAnimation animation;
	
	public LoadingPanel()
	{
		container = new DeckLayoutPanel();
		super.initWidget(container);
		setupLoadingContainer();
	}

	/**
	 * @return the isAnimated
	 */
	public boolean isAnimated() {
		return isAnimated;
	}

	/**
	 * @param isAnimated the isAnimated to set
	 */
	public void setAnimated(boolean isAnimated) {
		this.isAnimated = isAnimated;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected void initWidget(Widget widget) {
		add(widget);
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
		
		
		if (isAnimated) {
			innerWidget.getElement().getStyle().setOpacity(0);
			container.showWidget(innerWidget);
			FadeAnimation animation = getAnimation();
			animation.fadeIn(0, Speed.FAST);
		} else container.showWidget(innerWidget);
		//if (innerWidget instanceof RequiresResize) ((RequiresResize)innerWidget).onResize();
	}
	
	protected FadeAnimation getAnimation(){
		if (animation == null) animation = new FadeAnimation(innerWidget.getElement());
		return animation;
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
