package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.trait.Status;

/**
 * Partial implementation of <em>parameter objects</em> for domain objects.
 * <p>
 * A parameter object acts as a companion of a domain object, gathering all the parameters required to instantiate it.
 * It can be constructed incrementally and performs validation on behalf of the domain object.
 * 
 * @author Fabio Simeoni
 * 
 */
public abstract class DomainPO {

	private final String id;
	private Status status;

	protected DomainPO(String id) {
		this.id = id;
	}

	/**
	 * Returns the identifier parameter.
	 * 
	 * @return the identifier parameter
	 */
	public String id() {
		return id;
	}

	/**
	 * Returns the {@link Status} parameter.
	 * 
	 * @return the parameter
	 */
	public Status change() {
		return status;
	}

	public boolean isChangeset() {
		return status != null;
	}

	/**
	 * Sets the {@link Status} parameter.
	 * 
	 * @param the parameter
	 * 
	 * @throws IllegalArgumentException if the parameter is null or is incompatible with the other parameters.
	 */
	public void setChange(Status change) throws IllegalArgumentException {

		notNull("status", change);

		if (id() == null)
			throw new IllegalArgumentException("missing identifier: changeset does not identify a target object");

		this.status = change;
	}

}
