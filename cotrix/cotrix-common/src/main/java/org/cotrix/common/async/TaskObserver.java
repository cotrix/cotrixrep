package org.cotrix.common.async;


public interface TaskObserver<T> {
	
	abstract static class Abstract<S> implements TaskObserver<S> {
		
		@Override
		public void on(Exception e) {}
		
	}
	void on(T object);
	
	void on(Exception e);
	
	
}
