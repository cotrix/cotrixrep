package org.cotrix.common.async;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Task {

	private Map<Class<?>,Object> data = new HashMap<>();
	private Map<Class<?>,TaskObserver<Object>> observers = new HashMap<>();
	private Exception failure;
	
	@SuppressWarnings("all")
	public <T> void add(Class<T> type,TaskObserver<T> observer) {
		
		observers.put(type,(TaskObserver<Object>) observer);
	}
	
	void failed(Exception e) {
		
		failure=e;
		
		for (TaskObserver<?> observer : observers.values())
			try {
				observer.on(failure);
			}
			catch(Throwable ignore) {}
	}
	
	void put(Object o) {
		
		Class<?> type = o.getClass();
		
		data.put(type,o);
		
		TaskObserver<Object> observer = observers.get(type);
		
		if (observer!=null)
			observer.on(o);
	}
	
	<T> T get(Class<T> type) throws ExecutionException {
		if (failure==null)
			return type.cast(data.get(type));
		else
			throw new ExecutionException(failure);
	}
}
