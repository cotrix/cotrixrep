/**
 * 
 */
package org.cotrix.web.codelistmanager.server.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.action.Action;
import org.cotrix.action.CodelistAction;
import org.cotrix.web.codelistmanager.shared.ManagerUIFeature;
import org.cotrix.web.share.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIFeatures {
	
	public static Set<UIFeature> toFeatureSet(Collection<Action> actions)
	{
		HashSet<UIFeature> features = new HashSet<UIFeature>();
		for (Action action:actions) {
			CodelistAction codelistAction = map(action);
			List<ManagerUIFeature> actionFeatures = map(codelistAction);
			features.addAll(actionFeatures);
		}
		return features;
	}
	
	protected static List<ManagerUIFeature> map(CodelistAction codelistAction)
	{
		switch (codelistAction) {
			case EDIT: return Collections.singletonList(ManagerUIFeature.EDIT_METADATA);
			case LOCK: return Arrays.asList(ManagerUIFeature.LOCK_CODELIST);
			case UNLOCK: return Arrays.asList(ManagerUIFeature.UNLOCK_CODELIST);
			default: throw new IllegalArgumentException("No mapping for codelist action "+codelistAction);
		}
	}
	
	protected static CodelistAction map(Action action)
	{
		//TODO cache
		for (CodelistAction codelistAction:CodelistAction.values()) {
			if (codelistAction.getInnerAction().equals(action)) return codelistAction;
		}
		
		throw new IllegalArgumentException("The action "+action+" can't be mapped to a CodelistAction");
	}

}
