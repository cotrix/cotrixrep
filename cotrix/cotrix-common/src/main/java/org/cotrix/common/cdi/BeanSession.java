package org.cotrix.common.cdi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * An injectable application-level notion of session backed up at runtime by an HTTP session. 
 * @author Fabio Simeoni
 *
 */
@SuppressWarnings("serial")
public class BeanSession implements Serializable {

	private final Map<Class<?>,Object> data;
	
	public BeanSession() {
		this.data = new HashMap<Class<?>, Object>();
	}
	
	private BeanSession(Map<Class<?>,Object> data) {
		this.data = data;
	}
	
	/**
	 * Adds a bean of a given type to this session, replacing any other bean of the same type.
	 * @param type the type
	 * @param bean the bean
	 */
	public <T> void add(Class<T> type, T bean) {
		data.put(type,bean);
	}
	
	/**
	 * Returns <code>true</code> if a bean of a given type is in this session.
	 * @param type the type
	 * @return <code>true</code> if a bean of the given type is in this session
	 */
	public <T> boolean contains(Class<T> type) {
		return data.containsKey(type);
	}
	
	/**
	 * Returns a bean of the given type in this session.
	 * @param the type
	 * @return the bean
	 */
	public <T> T get(Class<T> type) {
		
		if (!contains(type))
			throw new IllegalStateException("no bean of type "+type+" is in session");

		return type.cast(data.get(type));
	}
	
	public void clear(Class<?> type) {
		
		data.remove(type);
	}
	
	public BeanSession copy() {
		return new BeanSession(this.data);
	}

}
