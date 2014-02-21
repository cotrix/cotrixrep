/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.manage.client.CotrixManagerAppGinInjector;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.EditType;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DataEditor<T> {
	
	public static <T> DataEditor<T> build(Object source)
	{
		return new DataEditor<T>(source, CotrixManagerAppGinInjector.INSTANCE.getEditorBus());
	}
	
	protected Object source;
	protected EventBus editorBus;

	/**
	 * @param source
	 * @param editorBus
	 */
	protected DataEditor(Object source, EventBus editorBus) {
		this.source = source;
		this.editorBus = editorBus;
	}

	public void added(T data)
	{
		fireEvent(new DataEditEvent<T>(data, EditType.ADD));
	}
	
	public void updated(T data)
	{
		fireEvent(new DataEditEvent<T>(data, EditType.UPDATE));
	}
	
	public void removed(T data)
	{
		fireEvent(new DataEditEvent<T>(data, EditType.REMOVE));
	}

	public void fireEvent(GwtEvent<?> event) {
		editorBus.fireEventFromSource(event, source);
	}
}
