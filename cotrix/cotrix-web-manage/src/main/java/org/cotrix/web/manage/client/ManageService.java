package org.cotrix.web.manage.client;

import java.util.List;
import java.util.Set;

import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.link.AttributeType;
import org.cotrix.web.common.shared.codelist.link.UILinkType;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.common.shared.feature.FeatureCarrier;
import org.cotrix.web.common.shared.feature.ResponseWrapper;
import org.cotrix.web.manage.shared.CodelistEditorSortInfo;
import org.cotrix.web.manage.shared.CodelistGroup;
import org.cotrix.web.manage.shared.Group;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RemoteServiceRelativePath("service/manageService")
public interface ManageService extends RemoteService {
	
	DataWindow<UICode> getCodelistCodes(String codelistId, Range range, CodelistEditorSortInfo sortInfo) throws ServiceException;
	Set<Group> getAttributesGroups(String codelistId) throws ServiceException;
	
	DataWindow<CodelistGroup> getCodelistsGrouped() throws ServiceException;
	
	UICodelistMetadata getMetadata(String codelistId) throws ServiceException;
	
	CodelistGroup createNewCodelistVersion(String codelistId, String newVersion) throws ServiceException;
	void removeCodelist(String codelistId) throws ServiceException;
	
	ResponseWrapper<String> getCodelistState(String codelistId) throws ServiceException;
	
	FeatureCarrier.Void lock(String codelistId) throws ServiceException;
	FeatureCarrier.Void unlock(String codelistId) throws ServiceException;
	FeatureCarrier.Void seal(String codelistId) throws ServiceException;
	
	public ModifyCommandResult modify(String codelistId, ModifyCommand command) throws ServiceException;
	
	Set<UIQName> getAttributeNames(String codelistId) throws ServiceException;
	
	CodelistGroup createNewCodelist(String name, String version) throws ServiceException;
	
	DataWindow<UILinkType> getCodelistLinkTypes(String codelistId) throws ServiceException;
	List<UICodelist> getCodelists() throws ServiceException;
	List<AttributeType> getAttributeTypes(String codelistId) throws ServiceException;

}
