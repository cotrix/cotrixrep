package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import java.util.UUID;

import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Status;

/**
 * Partial implementation of in-memory state beans.
 * 
 * @author Fabio Simeoni
 */
public class IdentifiedMS implements Identified.State {

	public static boolean testmode = false;
	
	/*
	  State beans can change across persistence bindings, but in-memory implementations are always used to:
	  	- create entities, including new versions;
	  	- create changesets;
	  	
	  Thus we use them for:
	  	 - id generation
	     - client input validation
						
	 */
	
	private final String id;
	private Status status;

	
	public IdentifiedMS() {
		this(UUID.randomUUID().toString());
	}
	
	public IdentifiedMS(String id) {
		
		valid("identifier",id);
		
		this.id = id;
		this.status=null;
	}

	public IdentifiedMS(String id, Status status) {
		
		notNull("status",status);
		valid("identifier",id);
		
		this.id=id;
		this.status=status;
	}

	public String id() {
		return id;
	}
	

	public Status status() {
		return status;
	}
	
	@Override
	public void status(Status status) {
		notNull("status", status);
		this.status = status;
	}

	//gotta be careful here and use immutable properties or state changes
	//will invalidate hash-based structures
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Identified.State))
			return false;
		Identified.State other = (Identified.State) obj;
		if (id == null) {
			if (other.id() != null)
				return false;
		} else if (!id.equals(other.id()) && !testmode)
			return false;
		return true;
	}
	
	
	

}
