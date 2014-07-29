/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;

import com.google.gwt.resources.client.CssResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkerStyleResource extends CssResource {
	
	public enum StyleElement {
		BACKGROUND_COLOR, TEXT_COLOR, HIGHLIGHT;
	}
	
	@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Style {
		StyleElement value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Style(StyleElement.BACKGROUND_COLOR)
	public @interface BackgroundColor {
		MarkerType value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Style(StyleElement.TEXT_COLOR)
	public @interface TextColor {
		MarkerType value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Style(StyleElement.HIGHLIGHT)
	public @interface Highlight {
		MarkerType value();
	}


}
