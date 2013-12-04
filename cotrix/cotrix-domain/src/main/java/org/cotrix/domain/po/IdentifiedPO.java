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
	
	public IdentifiedPO(Identified.State copy, boolean withId) {
		
		if (withId)
			id(copy.id());
		
		status = copy.status();
	}
	
	
	@Override
	public void id(String id) {
		
		valid("identifier",id);
		
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

}
