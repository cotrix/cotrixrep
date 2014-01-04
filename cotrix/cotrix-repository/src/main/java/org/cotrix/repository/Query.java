package org.cotrix.repository;

public interface Query<T,R> {

	R execute();
}
