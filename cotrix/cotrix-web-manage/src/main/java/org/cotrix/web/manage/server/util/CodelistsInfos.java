/**
 * 
 */
package org.cotrix.web.manage.server.util;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.lifecycle.State;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.web.common.server.util.Codelists;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.LifecycleState;
import org.cotrix.web.manage.shared.UICodelistInfo;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsInfos {
	
	public static UICodelistInfo toUICodelistInfo(CodelistCoordinates codelist, State state, boolean isUserInTeam) {
		UICodelistInfo uiCodelist = new UICodelistInfo();
		uiCodelist.setId(codelist.id());
		uiCodelist.setName(ValueUtils.safeValue(codelist.qname()));
		uiCodelist.setVersion(codelist.version());
		uiCodelist.setCreationDate(codelist.creationDate());
		LifecycleState lifecycleState = Codelists.getLifecycleState(state);
		uiCodelist.setState(lifecycleState);
		uiCodelist.setUserInTeam(isUserInTeam);
		
		return uiCodelist;
	}
	
	public static UICodelistInfo toUICodelistInfo(Codelist codelist, State state, boolean isUserInTeam) {
		return toUICodelistInfo(CodelistCoordinates.coordsOf(codelist), state, isUserInTeam);
	}

}
