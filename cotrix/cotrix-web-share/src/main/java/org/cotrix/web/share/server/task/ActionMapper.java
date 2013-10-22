/**
 * 
 */
package org.cotrix.web.share.server.task;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.action.Action;
import org.cotrix.common.cdi.Current;
import org.cotrix.user.PredefinedUsers;
import org.cotrix.user.User;
import org.cotrix.web.share.shared.feature.AuthenticationFeature;
import org.cotrix.web.share.shared.feature.UIFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Default
@Singleton
public class ActionMapper {
		
	protected Logger logger = LoggerFactory.getLogger(ActionMapper.class);
	
	@Inject
	@Current
	protected User user;
	
	protected Map<Action, Set<UIFeature>> mapping = new HashMap<Action, Set<UIFeature>>();
	
	public Set<UIFeature> mapActions(Collection<Action> actions)
	{
		Set<UIFeature> features = new HashSet<UIFeature>();
		for (Action action:actions) {
			Action generic = action.onAny();
			Set<UIFeature> actionFeatures = mapping.get(generic);
			if (actionFeatures!=null) features.addAll(actionFeatures);
			else {
				logger.warn("Action {} as generic {} has no UIFeatures", action, generic);
				logger.warn("Current registered actions:");
				for (Entry<Action, Set<UIFeature>> entry:mapping.entrySet()) logger.warn("{} -> {}", entry.getKey(), entry.getValue());
			}
		}
		
		if (user.equals(PredefinedUsers.guest)) features.add(AuthenticationFeature.CAN_LOGIN);
		else features.add(AuthenticationFeature.CAN_LOGOUT);
		
		return features;
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
