/**
 * 
 */
package org.cotrix.web.share.client.util;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.view.client.Range;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DataUpdatedEvent extends GwtEvent<DataUpdatedEvent.DataUpdatedHandler> {

	  /**
	   * Handler type.
	   */
	  private static Type<DataUpdatedHandler> TYPE;

	  public static interface DataUpdatedHandler extends EventHandler {


	    void onDataUpdated(DataUpdatedEvent event);
	  }
	  
	  /**
	   * Gets the type associated with this event.
	   *
	   * @return returns the handler type
	   */
	  public static Type<DataUpdatedHandler> getType() {
	    if (TYPE == null) {
	      TYPE = new Type<DataUpdatedHandler>();
	    }
	    return TYPE;
	  }

	  private final Range range;

	  /**
	   * Creates a {@link DataUpdatedEvent}.
	   *
	   * @param range the new range
	   */
	  public DataUpdatedEvent(Range range) {
	    this.range = range;
	  }

	  @Override
	  public final Type<DataUpdatedHandler> getAssociatedType() {
	    return TYPE;
	  }

	  /**
	   * Gets the new range.
	   *
	   * @return the new range
	   */
	  public Range getNewRange() {
	    return range;
	  }

	  @Override
	  public String toDebugString() {
	    return super.toDebugString() + getNewRange();
	  }


	  protected void dispatch(DataUpdatedHandler handler) {
	    handler.onDataUpdated(this);
	  }
	

}
