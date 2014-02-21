package org.cotrix.common.tx;

import java.io.Closeable;

public interface Transaction extends Closeable {

	void commit();
	
	@Override
	public void close();
}
