/**
 * 
 */
package org.cotrix.web.share.client.util;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;

/**
 * Duplicate of {@link com.google.gwt.view.client.SingleSelectionModel} that fires selection events also when the clicked item is the same.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SingleSelectionModel<T> extends AbstractSelectionModel<T> implements SetSelectionModel<T> {


	  private Object curKey;
	  private T curSelection;

	  // Pending selection change
	  private boolean newSelected;
	  private T newSelectedItem = null;
	  private boolean newSelectedPending;

	  /**
	   * Constructs a SingleSelectionModel without a key provider.
	   */
	  public SingleSelectionModel() {
	    super(null);
	  }

	  /**
	   * Constructs a SingleSelectionModel with the given key provider.
	   * 
	   * @param keyProvider an instance of ProvidesKey<T>, or null if the item
	   *          should act as its own key
	   */
	  public SingleSelectionModel(ProvidesKey<T> keyProvider) {
	    super(keyProvider);
	  }

	  @Override
	  public void clear() {
	    setSelected(getSelectedObject(), false);
	  }

	  /**
	   * Gets the currently-selected item.
	   * 
	   * @return the selected item
	   */
	  public T getSelectedObject() {
	    resolveChanges();
	    return curSelection;
	  }

	  @Override
	  public Set<T> getSelectedSet() {
	    Set<T> set = new HashSet<T>();
	    if (curSelection != null) {
	      set.add(curSelection);
	    }
	    return set;
	  }

	  @Override
	  public boolean isSelected(T item) {
	    resolveChanges();
	    if (curSelection == null || curKey == null || item == null) {
	      return false;
	    }
	    return curKey.equals(getKey(item));
	  }

	  @Override
	  public void setSelected(T item, boolean selected) {
	    // If we are deselecting an item that isn't actually selected, ignore it.
	    if (!selected) {
	      Object oldKey = newSelectedPending ? getKey(newSelectedItem) : curKey;
	      Object newKey = getKey(item);
	      if (!equalsOrBothNull(oldKey, newKey)) {
	        return;
	      }
	    }
	    newSelectedItem = item;
	    newSelected = selected;
	    newSelectedPending = true;
	    scheduleSelectionChangeEvent();
	  }

	  @Override
	  protected void fireSelectionChangeEvent() {
	    if (isEventScheduled()) {
	      setEventCancelled(true);
	    }
	    resolveChanges();
	  }

	  private boolean equalsOrBothNull(Object a, Object b) {
	    return (a == null) ? (b == null) : a.equals(b);
	  }

	  private void resolveChanges() {
	    if (!newSelectedPending) {
	      return;
	    }

	    Object key = getKey(newSelectedItem);
	    boolean sameKey = equalsOrBothNull(curKey, key);
	    boolean changed = false;
	    if (newSelected) {
	      changed = true;
	      curSelection = newSelectedItem;
	      curKey = key;
	    } else if (sameKey) {
	      changed = true;
	      curSelection = null;
	      curKey = null;
	    }

	    newSelectedItem = null;
	    newSelectedPending = false;

	    // Fire a selection change event.
	    if (changed) {
	      SelectionChangeEvent.fire(this);
	    }
	  }
}
