/**
 * 
 */
package org.cotrix.web.share.server.task;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.action.Action;
import org.cotrix.action.Actions;
import org.cotrix.action.CodelistAction;
import org.cotrix.action.GenericAction;
import org.cotrix.action.ResourceAction;
import org.cotrix.common.cdi.Current;
import org.cotrix.user.PredefinedUsers;
import org.cotrix.user.User;
import org.cotrix.web.share.shared.feature.AuthenticationFeature;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
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

	protected Map<Class<?>, Set<UIFeature>> anys = new HashMap<Class<?>, Set<UIFeature>>();

	protected Map<Class<?>, Map<Action, Set<UIFeature>>> mappings = new HashMap<Class<?>, Map<Action,Set<UIFeature>>>();

	protected <T extends Action> void addMapping(Class<T> type, T action, Set<UIFeature> features)
	{
		Map<Action, Set<UIFeature>> mapping = mappings.get(type);
		Set<UIFeature> any = anys.get(type);
		
		if (mapping == null) {
			mapping = new HashMap<Action, Set<UIFeature>>();
			mappings.put(type, mapping);
			any = new HashSet<UIFeature>();
			anys.put(type, any);
			mapping.put(Actions.action(Action.any), any);
		}

		mapping.put(action, features);
		any.addAll(features);
	}

	public void fillFeatures(FeatureCarrier carrier, String instanceId, Collection<Action> actions)
	{
		logger.trace("fillFeatures carrier: {} instanceId: {} actions: {}", carrier, instanceId, actions);
		Set<UIFeature> applicationFeatures = mapActions(actions, GenericAction.class);
		if (!applicationFeatures.isEmpty()) fillUserFeatures(applicationFeatures);
		if (!applicationFeatures.isEmpty()) carrier.setApplicationFeatures(applicationFeatures);
		
		Set<UIFeature> codelistFeatures = mapActions(actions, CodelistAction.class);
		if (!codelistFeatures.isEmpty()) carrier.setCodelistsFeatures(singletonMap(instanceId, codelistFeatures));
	}
	
	protected Map<String,Set<UIFeature>> singletonMap(String instanceId, Set<UIFeature> codelistFeatures)
	{
		Map<String,Set<UIFeature>> codelistsFeatures = new HashMap<String, Set<UIFeature>>();
		codelistsFeatures.put(instanceId, codelistFeatures);
		return codelistsFeatures;
	}
	
	protected void fillUserFeatures(Set<UIFeature> applicationFeatures)
	{
		logger.trace("fillUserFeatures current user: {}", user);
		if (user.id().equals(PredefinedUsers.guest.id())) applicationFeatures.add(AuthenticationFeature.CAN_LOGIN);
		else applicationFeatures.add(AuthenticationFeature.CAN_LOGOUT);
	}
	

	protected <T extends Action> Set<UIFeature> mapActions(Collection<Action> actions, Class<T> type)
	{
		Set<UIFeature> features = new HashSet<UIFeature>();
		Map<Action, Set<UIFeature>> mapping = mappings.get(type);
		if (mapping == null) {
			logger.warn("No mappings for type "+type);
			return features;
		}
		
		for (Action action:actions) {
			if (action instanceof ResourceAction) action = ((ResourceAction) action).onAny();
			Set<UIFeature> actionFeatures = mapping.get(action);
			if (actionFeatures!=null) {
				logger.trace("mapping {} to {}",action, actionFeatures);
				features.addAll(actionFeatures);
			}
		}

		return features;
	}

	public ActionMapBuilder<CodelistAction> map(CodelistAction action)
	{
		return new ActionMapBuilder<CodelistAction>(this, action, CodelistAction.class);
	}

	public ActionMapBuilder<GenericAction>  map(GenericAction action)
	{
		return new ActionMapBuilder<GenericAction>(this, action, GenericAction.class);
	}
}
