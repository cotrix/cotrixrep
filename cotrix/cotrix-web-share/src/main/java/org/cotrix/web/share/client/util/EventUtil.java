/**
 * 
 */
package org.cotrix.web.share.client.util;

import java.util.Set;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EventUtil {
	
	/**
	   * Sink events on the widget.
	   *
	   * @param widget the {@link Widget} that will handle the events
	   * @param typeNames the names of the events to sink
	   */
	  public static void sinkEvents(Widget widget, Set<String> typeNames) {
	    if (typeNames == null) {
	      return;
	    }

	    int eventsToSink = 0;
	    for (String typeName : typeNames) {
	      int typeInt = Event.getTypeInt(typeName);
	      if (typeInt < 0) {
	        widget.sinkBitlessEvent(typeName);
	      } else {
	        typeInt = sinkEvent(widget, typeName);
	        if (typeInt > 0) {
	          eventsToSink |= typeInt;
	        }
	      }
	    }
	    if (eventsToSink > 0) {
	      widget.sinkEvents(eventsToSink);
	    }
	  }

	  /**
	   * Get the event bits to sink for an event type.
	   *
	   * @param widget the {@link Widget} that will handle the events
	   * @param typeName the name of the event to sink
	   * @return the event bits to sink, or -1 if no events to sink
	   */
	  protected static int sinkEvent(Widget widget, String typeName) {
	    return Event.getTypeInt(typeName);
	  }

}
