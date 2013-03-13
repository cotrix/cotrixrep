package org.cotrix.domain.trait;

import javax.xml.namespace.QName;

/**
 * A named domain object.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Named {

	/**
	 * Returns the name of this object.
	 * @return the name
	 */
	QName name();

	/**
	 * A private extension of {@link Named}.
	 */
	public static interface Private<T extends Private<T>> extends Named,Attributed.Private<T> {}
}
