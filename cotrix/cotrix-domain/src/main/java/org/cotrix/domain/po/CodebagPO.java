package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import java.util.Collections;
import java.util.List;

import org.cotrix.common.Utils;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.Container;

/**
 * Initialisation parameters for {@link Codebag}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public final class CodebagPO extends VersionedPO {

	private Container.Private<Codelist.Private> lists = new Container.Private<Codelist.Private>(
			Collections.<Codelist.Private> emptyList());

	/**
	 * Creates an instance with an identifier.
	 * 
	 * @param id the identifier
	 */
	public CodebagPO(String id) {
		super(id);
	}

	/**
	 * Returns the {@link Codelist}s parameter.
	 * 
	 * @return the parameter
	 */
	public Container.Private<Codelist.Private> lists() {
		return lists;
	}

	/**
	 * Sets the {@link Codelist} parameter.
	 * 
	 * @param lists the paramter
	 */
	public void setLists(Container.Private<Codelist.Private> lists) {

		notNull("lists", lists);

		propagateChangeFrom(lists);

		this.lists = lists;
	}

	/**
	 * Sets the {@link Codelist} parameter.
	 * 
	 * @param lists the paramter
	 */
	public void setLists(List<Codelist> lists) {

		Container.Private<Codelist.Private> privateLists = new Container.Private<Codelist.Private>(Utils.reveal(lists,
				Codelist.Private.class));

		this.setLists(privateLists);
	}

}
