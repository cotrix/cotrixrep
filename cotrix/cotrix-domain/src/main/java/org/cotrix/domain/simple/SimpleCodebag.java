package org.cotrix.domain.simple;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.common.BaseGroup;
import org.cotrix.domain.pos.CodebagPO;
import org.cotrix.domain.utils.IdGenerator;
import org.cotrix.domain.versions.Version;

/**
 * Default {@link Codebag} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleCodebag extends SimpleVersionedObject<Codebag> implements Codebag {

	private final BaseGroup<Codelist> lists;

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
	public BaseGroup<Codelist> lists() {
		return lists;
	}
	
	protected void buildPO(CodebagPO po) {
		super.buildPO(po);
		po.setLists(lists());
	}
	
	@Override
	public SimpleCodebag copy(IdGenerator generator,Version version) {
		CodebagPO po = new CodebagPO(generator.generateId());
		buildPO(po);
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
