package org.cotrix.domain.memory;

import static java.util.UUID.*;
import static org.cotrix.common.CommonUtils.*;

import org.cotrix.domain.common.Status;
import org.cotrix.domain.trait.Identified;

//m-beans are used to create new entities, including new versions, and changesets.
//identifiers are generated right here, and most validation occurs in this hierarchy.
	  		

public class MIdentified implements Identified.Bean {

	public static boolean testmode = false;
	
	private final String id;
	private final Status status;

	//----------------------------------------------------
	
	//fresh id
	public MIdentified() {
		this(randomUUID().toString());
	}
	
	//given id (typically because preserved under versioning)
	public MIdentified(String id) {
		
		this.id=id;
		this.status=null;
	}

	//all changesets
	public MIdentified(String id, Status status) {
		
		notNull("status",status);
		valid("identifier",id);
		
		this.id=id;
		this.status=status;
	}

	
	//----------------------------------------------------
	
	
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
		} else if (!id.equals(other.id()) && !testmode) //check
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return super.toString()+":"+hashCode();
	}
	

}
