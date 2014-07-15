/**
 * 
 */
package org.cotrix.web.common.client.util;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
import java.math.BigDecimal;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;

/**
 * Adapted from http://map-notes.blogspot.it/2012/11/fade-animation.html
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FadeAnimation extends Animation {
	
	public interface AnimationListener {
		public void onComplete();
	}
	
	public enum Speed {
		IMMEDIATE(0),
		VERY_FAST(500),
		FAST(1000),
		NORMAL(1500),
		SLOW(2000);
		
		private int time;

		/**
		 * @param time
		 */
		private Speed(int time) {
			this.time = time;
		}

		/**
		 * @return the time
		 */
		public int getTime() {
			return time;
		}		
	}
	
	public static final double INVISIBLE_OPACITY = 0.0;
	public static final double VISIBLE_OPACITY = 1.0;
	
	private Element element;
	private double opacityIncrement;
	private double targetOpacity;
	private double baseOpacity;
	
	private double visibleOpacity;
	private double invisibleOpacity;
	
	private boolean setDisplayProperty = false;
	private String oldDisplayValue;
	
	private AnimationListener listener;

	public FadeAnimation(Element element) {
		this(element, VISIBLE_OPACITY, INVISIBLE_OPACITY, false);
	}
	
	public FadeAnimation(Element element, boolean setDisplayProperty) {
		this(element, VISIBLE_OPACITY, INVISIBLE_OPACITY, setDisplayProperty);
	}
	
	public FadeAnimation(Element element, double visibleOpacity, double invisibleOpacity) {
		this(element, VISIBLE_OPACITY, INVISIBLE_OPACITY, false);
	}
	
	public FadeAnimation(Element element, double visibleOpacity, double invisibleOpacity, boolean setDisplayProperty) {
		this.element = element;
		this.oldDisplayValue = element.getStyle().getDisplay();
		this.visibleOpacity = visibleOpacity;
		this.invisibleOpacity = invisibleOpacity;
		this.setDisplayProperty = setDisplayProperty;
	}

	@Override
	protected void onUpdate(double progress) {
		element.getStyle().setOpacity(baseOpacity + progress * opacityIncrement);
	}

	@Override
	protected void onComplete() {
		super.onComplete();
		element.getStyle().setOpacity(targetOpacity);
		
		if (targetOpacity == VISIBLE_OPACITY) {
			element.getStyle().setVisibility(Visibility.VISIBLE);
		}
		
		if (targetOpacity == INVISIBLE_OPACITY) {
			element.getStyle().setVisibility(Visibility.HIDDEN);
			if (setDisplayProperty) element.getStyle().setProperty("display", "none");
		}
		
		
		if (listener!=null) listener.onComplete();
	}
	
	public AnimationListener getListener() {
		return listener;
	}

	public void setListener(AnimationListener listener) {
		this.listener = listener;
	}

	public void setVisibility(boolean visible, Speed speed)
	{
		if (!(visible ^ isElementVisible())) return;
		if (visible) fadeIn(speed);
		else fadeOut(speed);
	}
	
	public boolean isElementVisible() {
		String opacityValue = element.getStyle().getOpacity();
		if (opacityValue == null) return true;
		try {
			double opacity = new BigDecimal(opacityValue).doubleValue();
			return opacity == visibleOpacity;
		} catch(NumberFormatException e) {
			return true;
		}
	}
	
	public void fadeOut(Speed speed)
	{
		cancel();
		element.getStyle().setOpacity(1);
		element.getStyle().setVisibility(Visibility.VISIBLE);
		fade(speed.getTime(), invisibleOpacity);
	}
	
	public void fadeIn(Speed speed)
	{
		fadeIn(invisibleOpacity, speed);
	}
	
	public void fadeIn(double startingOpacity, Speed speed)
	{
		cancel();
		if (setDisplayProperty) element.getStyle().setProperty("display", oldDisplayValue);
		element.getStyle().setOpacity(startingOpacity);
		element.getStyle().setVisibility(Visibility.VISIBLE);
		fade(speed.getTime(), 1);
	}

	public void fade(int duration, double targetOpacity) {
		if(targetOpacity > 1.0) {
			targetOpacity = 1.0;
		}
		if(targetOpacity < 0.0) {
			targetOpacity = 0.0;
		}
		this.targetOpacity = targetOpacity;
		String opacityStr = element.getStyle().getOpacity();
		try {
			baseOpacity = new BigDecimal(opacityStr).doubleValue();
			opacityIncrement = targetOpacity - baseOpacity;
			run(duration);
		} catch(NumberFormatException e) {
			// set opacity directly
			onComplete();
		}
	}

}
