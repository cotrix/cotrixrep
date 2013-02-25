package org.cotrix.domain.pos;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.common.BaseGroup;

/**
 * A set of parameters required to create a {@link Codebag}.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodebagPO extends VersionedPO {
	
	private BaseGroup<Codelist> lists = new BaseGroup<Codelist>(Collections.<Codelist>emptyList());

	/**
	 * Creates an instance with an identifier.
	 * @param id the identifier
	 */
	public CodebagPO(String id) {
		super(id);
	}
	
	/**
	 * Returns the {@link Codelist}s parameter. 
	 * @return the parameter
	 */
	public BaseGroup<Codelist> lists() {
		return lists;
	}

	/**
	 * Sets the {@link Codelist} parameter.
	 * @param lists the paramter
	 */
	public void setLists(BaseGroup<Codelist> lists) {
		
		notNull("lists",lists);
		
		validateDeltaParameter(lists);
		
		this.lists = lists;
	}
	
}
