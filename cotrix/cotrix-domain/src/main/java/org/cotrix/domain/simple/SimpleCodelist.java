package org.cotrix.domain.simple;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.po.CodelistPO;
import org.cotrix.domain.primitive.Container;
import org.cotrix.domain.primitive.PContainer;
import org.cotrix.domain.simple.primitive.SimpleVersioned;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.version.Version;

/**
 * Default {@link Codelist} implementation.
 * 
 * @author Fabio Simeoni
 * 
 */
public class SimpleCodelist extends SimpleVersioned<Codelist.Private> implements Codelist.Private {

	private final PContainer<Code.Private> codes;
	private final PContainer<CodelistLink.Private> links;

	/**
	 * Creates an instance with given identifier,name, codes, attributes, and version.
	 * 
	 * @param id the identifier
	 * @param name the name
	 * @param codes the codes
	 * @param attributes the attributes
	 * @param version the version
	 */
	public SimpleCodelist(CodelistPO param) {
		super(param);
		this.codes = param.codes();
		this.links = param.links();
	}

	@Override
	public PContainer<Code.Private> codes() {
		return codes;
	}
	
	@Override
	public Container<CodelistLink.Private> links() {
		return links;
	}

	protected void buildPO(IdGenerator generator,CodelistPO po) {
		super.fillPO(generator,po);
		po.setCodes(codes().copy(generator));
		po.setLinks(links.copy(generator));
	}

	@Override
	protected SimpleCodelist copy(IdGenerator generator, Version version) throws IllegalArgumentException,
			IllegalStateException {
		CodelistPO po = new CodelistPO(generator.generateId());
		buildPO(generator,po);
		po.setVersion(version);
		return new SimpleCodelist(po);
	}

	@Override
	public String toString() {
		return "Codelist [id="+id()+", name=" + name() + ", codes=" + codes + ", attributes=" + attributes() + ", version="
				+ version() + "]";
	}

	@Override
	public void update(Codelist.Private delta) throws IllegalArgumentException, IllegalStateException {
		super.update(delta);
		this.codes().update(delta.codes());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codes == null) ? 0 : codes.hashCode());
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
		SimpleCodelist other = (SimpleCodelist) obj;
		if (codes == null) {
			if (other.codes != null)
				return false;
		} else if (!codes.equals(other.codes))
			return false;
		return true;
	}

}
