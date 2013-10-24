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
import org.cotrix.action.InstanceAction;
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

	protected Map<Action, Set<UIFeature>> mappings = new HashMap<Action,Set<UIFeature>>();

	protected <T extends Action> void addMapping(Class<T> type, T action, Set<UIFeature> features)
	{
		Set<UIFeature> any = anys.get(type);

		if (any == null) {
			any = new HashSet<UIFeature>();
			anys.put(type, any);
			mappings.put(Actions.action(Action.any), any);
		}

		mappings.put(action, features);
		any.addAll(features);
	}

	public void fillFeatures(FeatureCarrier carrier, String instanceId, Collection<Action> actions)
	{
		logger.trace("fillFeatures carrier: {} instanceId: {} actions: {}", carrier.getClass(), instanceId, actions);
		Set<UIFeature> applicationFeatures = mapGenericActions(actions);
		fillUserFeatures(applicationFeatures);
		if (!applicationFeatures.isEmpty()) carrier.setApplicationFeatures(applicationFeatures);

		if (instanceId!=null) {
			Set<UIFeature> codelistFeatures = mapInstanceActions(actions, instanceId);
			carrier.setCodelistsFeatures(singletonMap(instanceId, codelistFeatures));
		}
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

	protected Set<UIFeature> mapGenericActions(Collection<Action> actions)
	{
		logger.trace("mapGenericActions actions {}",actions);
		Set<UIFeature> features = new HashSet<UIFeature>();
		for (Action action:actions) {
			if (!(action instanceof InstanceAction)) {
				Set<UIFeature> actionFeatures = mappings.get(action);
				if (actionFeatures!=null) {
					logger.trace("mapping {} to {}", action, actionFeatures);
					features.addAll(actionFeatures);
				} else logger.warn("No mappings for action "+action);
			}
		}

		return features;
	}


	protected Set<UIFeature> mapInstanceActions(Collection<Action> actions, String instanceId)
	{
		logger.trace("mapInstanceActions actions {}, instanceId {}",actions, instanceId);
		Set<UIFeature> features = new HashSet<UIFeature>();

		for (Action action:actions) {
			if (action instanceof InstanceAction) {
				InstanceAction instanceAction = (InstanceAction) action;
				if (!instanceAction.instance().equals(instanceId)) continue;
				action = instanceAction.onAny();
				Set<UIFeature> actionFeatures = mappings.get(action);
				if (actionFeatures!=null) {
					logger.trace("mapping {} to {}",action, actionFeatures);
					features.addAll(actionFeatures);
				} else logger.warn("No mappings for action "+action);
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
