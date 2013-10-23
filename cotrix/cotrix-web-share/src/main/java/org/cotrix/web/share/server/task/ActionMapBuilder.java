/**
 * 
 */
package org.cotrix.web.share.server.task;

import java.util.Arrays;
import java.util.HashSet;

import org.cotrix.action.Action;
import org.cotrix.web.share.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ActionMapBuilder<T extends Action> {
	
	protected ActionMapper mapper;
	protected T action;
	protected Class<T> type;
	
	/**
	 * @param action
	 */
	protected ActionMapBuilder(ActionMapper mapper, T action, Class<T> type) {
		this.mapper = mapper;
		this.action = action;
		this.type = type;
	}
	
	public void to(UIFeature ... features)
	{
		if (features==null || features.length == 0) throw new IllegalArgumentException("You should specify one or more features");
		mapper.addMapping(type, action, new HashSet<UIFeature>(Arrays.asList(features)));
	}

}
