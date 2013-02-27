package org.cotrix.domain.simple;

import static org.cotrix.domain.utils.Utils.*;

import org.cotrix.domain.pos.VersionedPO;
import org.cotrix.domain.primitives.VersionedObject;
import org.cotrix.domain.utils.IdGenerator;
import org.cotrix.domain.versions.Version;

/**
 * A partial implementation of {@link VersionedObject}.
 * 
 * @author Fabio Simeoni
 * 
 * <T> the type of the entity
 *
 */
abstract class SimpleVersionedObject<T extends VersionedObject<T>> extends SimpleAttributedObject<T> implements VersionedObject<T> {

	private final Version version;

	/**
	 * Creates an instance with a given identifier, name, attributes, and version.
	 * @param id the identifier
	 * @param name the name
	 * @param attributes the attributes
	 * @param version the version
	 */
	public SimpleVersionedObject(VersionedPO param) {
		super(param);
		this.version=param.version();
	}
	
	@Override
	public String version() {
		return version.value();
	}
	
	protected void fillPO(VersionedPO po) {
		super.fillPO(po);
		po.setVersion(version);
	}
	
	public void update(T delta) throws IllegalArgumentException ,IllegalStateException {
		
		super.update(delta);
		
		//name has changed?
		if (!delta.version().equals(this.version()))
			throw new IllegalArgumentException("cannot change the version ("+version()+") of entity "+id()+". Versioning is performed by copy");
	};
	
	@Override
	public T bump(IdGenerator generator,String version) {
		
		notNull("version",version);
		
		if (id()==null)
			throw new IllegalStateException("object cannot be versioned because it has no identifier");
		
		Version newVersion = this.version.bumpTo(version);
		
		return copy(generator,newVersion);
	}
	
	@Override
	public T copy(IdGenerator generator) {
		return copy(generator,version);
	}
	
	protected abstract T copy(IdGenerator generator,Version version);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleVersionedObject<?> other = (SimpleVersionedObject<?>) obj;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	
	

	
}
