package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;
import java.util.List;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.primitive.PContainer;
import org.cotrix.domain.utils.Utils;

/**
 * Initialisation parameters for {@link Codebag}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodebagPO extends VersionedPO {
	
	private PContainer<Codelist.Private> lists = new PContainer<Codelist.Private>(Collections.<Codelist.Private>emptyList());

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
	public PContainer<Codelist.Private> lists() {
		return lists;
	}

	/**
	 * Sets the {@link Codelist} parameter.
	 * @param lists the paramter
	 */
	public void setLists(PContainer<Codelist.Private> lists) {
		
		notNull("lists",lists);
		
		propagateChangeFrom(lists);
		
		this.lists = lists;
	}
	
	/**
	 * Sets the {@link Codelist} parameter.
	 * @param lists the paramter
	 */
	public void setLists(List<Codelist> lists) {
		
		PContainer<Codelist.Private> privateLists = new PContainer<Codelist.Private>(Utils.reveal(lists,Codelist.Private.class));
		
		this.setLists(privateLists);
	}
	
}
