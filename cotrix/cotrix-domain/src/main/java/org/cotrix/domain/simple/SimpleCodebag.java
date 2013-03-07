package org.cotrix.domain.simple;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.po.CodebagPO;
import org.cotrix.domain.primitive.container.Bag;
import org.cotrix.domain.simple.primitive.SimpleVersionedEntity;
import org.cotrix.domain.utils.IdGenerator;
import org.cotrix.domain.version.Version;

/**
 * Default {@link Codebag} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleCodebag extends SimpleVersionedEntity<Codebag> implements Codebag {

	private final Bag<Codelist> lists;

	/**
	 * Creates an instance with a given identifier,name, code lists, and attributes.
	 * @param id identifier
	 * @param name the name
	 * @param lists the code lists
	 * @param attributes the attributes
	 * 
	 */
	public SimpleCodebag(CodebagPO params) {
		super(params);
		this.lists=params.lists();
	}
	
	@Override
	public Bag<Codelist> lists() {
		return lists;
	}
	
	protected void buildPO(IdGenerator generator,CodebagPO po) {
		super.fillPO(po);
		po.setLists(lists().copy(generator));
	}
	
	@Override
	public SimpleCodebag copy(IdGenerator generator,Version version) {
		CodebagPO po = new CodebagPO(generator.generateId());
		buildPO(generator,po);
		po.setVersion(version);
		return new SimpleCodebag(po);
	}

	@Override
	public String toString() {
		return "CodeBag [name=" + name() + ", lists=" + lists + ", attributes=" + attributes() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((lists == null) ? 0 : lists.hashCode());
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
		SimpleCodebag other = (SimpleCodebag) obj;
		if (lists == null) {
			if (other.lists != null)
				return false;
		} else if (!lists.equals(other.lists))
			return false;
		return true;
	}

	
	
}
