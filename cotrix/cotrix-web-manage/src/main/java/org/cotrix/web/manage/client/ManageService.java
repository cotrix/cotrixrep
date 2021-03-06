package org.cotrix.web.manage.client;

import java.util.List;
import java.util.Set;

import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.codelist.LifecycleState;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier;
import org.cotrix.web.common.shared.feature.ResponseWrapper;
import org.cotrix.web.manage.shared.CodelistEditorSortInfo;
import org.cotrix.web.manage.shared.CodelistRemoveCheckResponse;
import org.cotrix.web.manage.shared.CodelistValueTypes;
import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UICodelistInfo;
import org.cotrix.web.manage.shared.UILinkDefinitionInfo;
import org.cotrix.web.manage.shared.UILogbookEntry;
import org.cotrix.web.manage.shared.filter.FilterOption;
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
	
	DataWindow<UICode> getCodelistCodes(String codelistId, Range range, CodelistEditorSortInfo sortInfo, List<FilterOption> filterOptions) throws ServiceException;
	
	List<UICodelistInfo> getCodelistsInfos() throws ServiceException;
	
	UICodelistMetadata getMetadata(String codelistId) throws ServiceException;
	
	List<UILogbookEntry> getLogbookEntries(String codelistId) throws ServiceException;
	void removeLogbookEntry(String codelistId, String entryId) throws ServiceException;
	
	ResponseWrapper<LifecycleState> getCodelistState(String codelistId) throws ServiceException;
	
	AbstractFeatureCarrier.Void lock(String codelistId) throws ServiceException;
	AbstractFeatureCarrier.Void unlock(String codelistId) throws ServiceException;
	AbstractFeatureCarrier.Void seal(String codelistId) throws ServiceException;
	AbstractFeatureCarrier.Void unseal(String codelistId) throws ServiceException;
	
	public ModifyCommandResult modify(String codelistId, ModifyCommand command) throws ServiceException;
	
	Set<UIQName> getAttributeNames(String codelistId) throws ServiceException;
	
	UICodelistInfo createNewCodelist(String name, String version) throws ServiceException;
	
	DataWindow<UILinkDefinition> getCodelistLinkTypes(String codelistId) throws ServiceException;
	List<UICodelist> getCodelists() throws ServiceException;
	CodelistValueTypes getCodelistValueTypes(String codelistId) throws ServiceException;
	
	List<UILinkDefinitionInfo> getLinkTypes(String codelistId) throws ServiceException;
	List<UICodeInfo> getCodes(String codelistId, String linkTypeId) throws ServiceException;
	
	public CodelistRemoveCheckResponse canUserRemove(String codelistId) throws ServiceException;
	
	DataWindow<UIAttributeDefinition> getCodelistAttributeTypes(String codelistId) throws ServiceException;
	
	List<UIAttributeDefinition> getMarkersAttributeTypes() throws ServiceException;

}
