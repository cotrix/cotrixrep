package org.cotrix.common.tx;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Transactions {

	@Inject
	private Instance<Transaction> tx;
	
	
	public Transaction open() {
		return tx.get();
	}
		
	
}
