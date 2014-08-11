package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.UUID;

import org.cotrix.domain.common.Status;
import org.cotrix.domain.trait.Identified;

/**
 * Partial implementation of in-memory state beans.
 * 
 * @author Fabio Simeoni
 */
public class MIdentified implements Identified.Bean {

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

	
	public MIdentified() {
		this(UUID.randomUUID().toString());
	}
	
	public MIdentified(String id) {
		
		valid("identifier",id);
		
		this.id = id;
		this.status=null;
	}

	public MIdentified(String id, Status status) {
		
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
		if (!(obj instanceof Identified.Bean))
			return false;
		Identified.Bean other = (Identified.Bean) obj;
		if (id == null) {
			if (other.id() != null)
				return false;
		} else if (!id.equals(other.id()) && !testmode)
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return super.toString()+":"+hashCode();
	}
	

}
