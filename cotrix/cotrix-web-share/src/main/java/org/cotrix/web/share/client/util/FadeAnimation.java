/**
 * 
 */
package org.cotrix.web.share.client.util;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
import java.math.BigDecimal;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.Element;

/**
 * Adapted from http://map-notes.blogspot.it/2012/11/fade-animation.html
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FadeAnimation extends Animation {
	
	private Element element;
	private double opacityIncrement;
	private double targetOpacity;
	private double baseOpacity;

	public FadeAnimation(Element element) {
		this.element = element;
	}

	@Override
	protected void onUpdate(double progress) {
		element.getStyle().setOpacity(baseOpacity + progress * opacityIncrement);
	}

	@Override
	protected void onComplete() {
		super.onComplete();
		element.getStyle().setOpacity(targetOpacity);
	}
	
	public void fadeOut()
	{
		cancel();
		element.getStyle().setOpacity(1);
		fade(2000, 0);
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
