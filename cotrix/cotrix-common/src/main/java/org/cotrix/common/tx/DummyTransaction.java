package org.cotrix.common.tx;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alternative @Priority(DEFAULT)
public class DummyTransaction implements Transaction {

	private static Logger log = LoggerFactory.getLogger(DummyTransaction.class);
	
	public DummyTransaction() {
		log.trace("transaction start");
	}
	
	@Override
	public void close() {
		log.trace("transaction end");
		
	}

	@Override
	public void commit() {
		log.trace("transaction commit");
	}

	
}
