package org.cotrix.common.tx;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

@Alternative @Priority(DEFAULT)
public class DummyTransaction implements Transaction {

	public DummyTransaction() {
	}
	
	@Override
	public void close() {
	}

	@Override
	public void commit() {
	}

	
}
