/**
 * 
 */
package org.cotrix.web.share.server.task;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Default;
import javax.inject.Singleton;

import org.cotrix.action.Action;
import org.cotrix.web.share.shared.feature.UIFeature;

import com.google.gwt.dev.util.collect.HashSet;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Default
@Singleton
public class ActionMapper {
	
	protected Map<Action, Set<UIFeature>> mapping = new HashMap<Action, Set<UIFeature>>();
	
	public Set<UIFeature> mapActions(Collection<Action> actions)
	{
		
		return Collections.emptySet();
	}
	
	public ActionMapBuilder map(Action action)
	{
		return new ActionMapBuilder(this, action);
	}
	
	public void addMapping(Action action, UIFeature ... features)
	{
		mapping.put(action, new HashSet<UIFeature>(Arrays.asList(features)));
	}

}
