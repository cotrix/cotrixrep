package org.cotrix.domain.pos;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.primitives.BaseGroup;

/**
 * A set of parameters required to create a {@link Codelist}.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistPO extends VersionedPO {
	
	private BaseGroup<Code> codes = new BaseGroup<Code>(Collections.<Code>emptyList());

	/**
	 * Creates an instance with an identifier.
	 * @param id the identifier
	 */
	public CodelistPO(String id) {
		super(id);
	}
	
	/**
	 * Returns the codes parameter.
	 * @return the codes parameter
	 */
	public BaseGroup<Code> codes() {
		return codes;
	}

	/**
	 * Sets the codes parameter.
	 * @param codes the codes parameter
	 */
	public void setCodes(BaseGroup<Code> codes) {
		
		notNull("codes",codes);
		
		propagateChangeFrom(codes);
		
		this.codes = codes;
	}
}
