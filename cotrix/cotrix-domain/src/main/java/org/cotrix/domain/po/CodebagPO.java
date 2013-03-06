package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.primitive.container.MutableContainer;

/**
 * Initialisation parameters for {@link Codebag}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodebagPO extends VersionedPO {
	
	private MutableContainer<Codelist> lists = new MutableContainer<Codelist>(Collections.<Codelist>emptyList());

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
	public MutableContainer<Codelist> lists() {
		return lists;
	}

	/**
	 * Sets the {@link Codelist} parameter.
	 * @param lists the paramter
	 */
	public void setLists(MutableContainer<Codelist> lists) {
		
		notNull("lists",lists);
		
		propagateChangeFrom(lists);
		
		this.lists = lists;
	}
	
}
