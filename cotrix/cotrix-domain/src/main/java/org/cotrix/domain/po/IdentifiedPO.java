package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.trait.Identified;
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
public abstract class IdentifiedPO implements Identified.State {

	private String id;
	private Status status;

	protected IdentifiedPO(String id) {
		this.id = id;
	}
	
	@Override
	public void id(String id) {
		this.id=id;
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
	public Status status() {
		return status;
	}

	/**
	 * Sets the {@link Status} parameter.
	 * 
	 * @param the parameter
	 * 
	 * @throws IllegalArgumentException if the parameter is null or is incompatible with the other parameters.
	 */
	public void status(Status change) throws IllegalArgumentException {

		notNull("status", change);

		if (id() == null)
			throw new IllegalArgumentException("missing identifier: changeset does not identify a target object");

		this.status = change;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifiedPO other = (IdentifiedPO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	
	

}
