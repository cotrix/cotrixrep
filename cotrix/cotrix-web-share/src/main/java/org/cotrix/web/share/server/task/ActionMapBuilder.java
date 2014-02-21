/**
 * 
 */
package org.cotrix.web.share.server.task;

import org.cotrix.action.Action;
import org.cotrix.web.share.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ActionMapBuilder {
	
	protected ActionMapper mapper;
	protected Action action;
	
	/**
	 * @param action
	 */
	protected ActionMapBuilder(ActionMapper mapper, Action action) {
		this.mapper = mapper;
		this.action = action;
	}
	
	public void to(UIFeature ... features)
	{
		if (features==null || features.length == 0) throw new IllegalArgumentException("You should specify one or more features");
		mapper.addMapping(action, features);
	}

}
