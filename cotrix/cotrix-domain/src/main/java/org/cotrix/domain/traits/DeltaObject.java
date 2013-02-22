package org.cotrix.domain.traits;

import org.cotrix.domain.common.Delta;

public interface DeltaObject {

	/**
	 * Returns update status of the object.
	 * @return the status
	 */
	Delta delta();
	
	
	/**
	 * Sets the update status of the object.
	 * @param status the status
	 */
	void setDelta(Delta status);
}
