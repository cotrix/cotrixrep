package org.cotrix.web.manage.server;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.repository.CodelistQueries.*;
import static org.cotrix.web.manage.shared.ManagerUIFeature.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;

import org.cotrix.action.CodelistAction;
import org.cotrix.action.MainAction;
import org.cotrix.action.ResourceType;
import org.cotrix.action.events.CodelistActionEvents;
import org.cotrix.application.ChangelogService;
import org.cotrix.application.ValidationService;
import org.cotrix.application.VersioningService;
import org.cotrix.application.logbook.Logbook;
import org.cotrix.application.logbook.Logbook.Entry;
import org.cotrix.application.logbook.LogbookService;
import org.cotrix.common.BeanSession;
import org.cotrix.common.events.Current;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.attributes.CommonDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.User;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.impl.DefaultLifecycleStates;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.UserRepository;
import org.cotrix.web.common.server.CotrixRemoteServlet;
import org.cotrix.web.common.server.async.ProgressService;
import org.cotrix.web.common.server.task.ActionMapper;
import org.cotrix.web.common.server.task.CodelistTask;
import org.cotrix.web.common.server.task.ContainsTask;
import org.cotrix.web.common.server.task.Id;
import org.cotrix.web.common.server.util.AttributeDefinitions;
import org.cotrix.web.common.server.util.Codelists;
import org.cotrix.web.common.server.util.LinkDefinitions;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.LifecycleState;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.common.shared.codelist.linkdefinition.AttributeValue;
import org.cotrix.web.common.shared.codelist.linkdefinition.LinkValue;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier.Void;
import org.cotrix.web.common.shared.feature.ApplicationFeatures;
import org.cotrix.web.common.shared.feature.ResponseWrapper;
import org.cotrix.web.manage.client.ManageService;
import org.cotrix.web.manage.server.modify.ChangesetUtil;
import org.cotrix.web.manage.server.modify.ModifyCommandHandler;
import org.cotrix.web.manage.server.util.CodelistsInfos;
import org.cotrix.web.manage.server.util.LogbookEntries;
import org.cotrix.web.manage.shared.CodelistEditorSortInfo;
import org.cotrix.web.manage.shared.CodelistRemoveCheckResponse;
import org.cotrix.web.manage.shared.CodelistValueTypes;
import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UICodelistInfo;
import org.cotrix.web.manage.shared.UILinkDefinitionInfo;
import org.cotrix.web.manage.shared.UILogbookEntry;
import org.cotrix.web.manage.shared.filter.FilterOption;
import org.cotrix.web.manage.shared.filter.MarkerFilterOption;
import org.cotrix.web.manage.shared.filter.SessionStartedOption;
import org.cotrix.web.manage.shared.filter.SinceCreationOption;
import org.cotrix.web.manage.shared.filter.SinceDateOption;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The server side implementation of the RPC serialiser.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
@ContainsTask
public class ManageServiceImpl implements ManageService {

	public static class Servlet extends CotrixRemoteServlet {

		@Inject
		protected ManageServiceImpl bean;

		@Override
		public Object getBean() {
			return bean;
		}
	}

	protected Logger logger = LoggerFactory.getLogger(ManageServiceImpl.class);

	@Inject
	private ActionMapper mapper;

	@Inject
	private CodelistRepository repository;

	@Inject
	private ModifyCommandHandler commandHandler;

	@Inject
	private VersioningService versioningService;

	@Inject
	private LifecycleService lifecycleService;

	@Inject
	private LogbookService logbookService;

	@Inject
	private Event<CodelistActionEvents.CodelistEvent> events;

	@Inject @Current
	private BeanSession session;
	
	@Inject
	private HttpSession httpSession;

	@Inject @Current
	private User currentUser;

	@Inject
	private UserRepository userRepository;

	@Inject
	private ProgressService progressService;

	@Inject
	private ValidationService validationService;

	@Inject
	private ChangelogService changelogService;

	/** 
	 * {@inheritDoc}
	 */
	@PostConstruct
	public void init() {
		mapper.map(VIEW).to(VIEW_CODELIST, VIEW_METADATA);
		mapper.map(EDIT).to(EDIT_METADATA, EDIT_CODELIST, ADD_CODE, REMOVE_CODE, ApplicationFeatures.VERSION_CODELIST);
		mapper.map(LOCK).to(LOCK_CODELIST);
		mapper.map(UNLOCK).to(UNLOCK_CODELIST);
		mapper.map(SEAL).to(SEAL_CODELIST);
		mapper.map(UNSEAL).to(UNSEAL_CODELIST);
		mapper.map(REMOVE).to(ApplicationFeatures.REMOVE_CODELIST);
		mapper.map(REMOVE_LOGBOOK_ENTRY).to(REMOVE_LOGBOOKENTRY);
		mapper.map(MainAction.CREATE_CODELIST).to(ApplicationFeatures.CREATE_CODELIST);
	}

	@Override
	public List<UICodelistInfo> getCodelistsInfos() throws ServiceException {
		logger.trace("getCodelistsInfos");

		List<UICodelistInfo> codelistInfos = new ArrayList<>();

		Iterable<CodelistCoordinates> codelists = repository.get(allListCoordinates().sort(byCoordinateName()));

		List<String> codelistIds = new ArrayList<>();
		for (CodelistCoordinates codelist:codelists) codelistIds.add(codelist.id());
		Map<String, Lifecycle> lifecycles = lifecycleService.lifecyclesOf(codelistIds);

		FingerPrint fp = currentUser.fingerprint();

		for (CodelistCoordinates codelist:codelists) {

			boolean isUserInTeam = !fp.allRolesOver(codelist.id(), ResourceType.codelists).isEmpty();

			Lifecycle lifecycle = lifecycles.get(codelist.id());
			UICodelistInfo codelistInfo = CodelistsInfos.toUICodelistInfo(codelist, lifecycle.state(), isUserInTeam);

			codelistInfos.add(codelistInfo);
		}


		return codelistInfos;
	}

	@Override
	@CodelistTask(VIEW)
	public DataWindow<UICode> getCodelistCodes(@Id String codelistId, com.google.gwt.view.client.Range range, CodelistEditorSortInfo sortInfo, List<FilterOption> filterOptions) throws ServiceException {
		logger.trace("getCodelistRows codelistId {}, range: {} sortInfo: {} filterOptions: {}", codelistId, range, sortInfo, filterOptions);

		int start = range.getStart() + 1;
		int end = range.getStart() + range.getLength();
		logger.trace("query range from: {} to: {}", start ,end);

		Codelist codelist = repository.lookup(codelistId);

		MultiQuery<Codelist, Code> query = null;
		CodelistSizeProvider codelistSizeProvider = null;
		
		if (!filterOptions.isEmpty()) {
			FilterOption option = filterOptions.get(0);
			if (option instanceof MarkerFilterOption) {
				MarkerFilterOption markerOption = (MarkerFilterOption)option;
				CommonDefinition[] definitions = getDefinitions(markerOption.getDefinitionsNames());
				query = codesWith(definitions).in(codelistId);
				codelistSizeProvider = new CodelistSizeProvider.CounterCodelistSizeProvider(repository.get(codesWith(definitions).in(codelistId)));
			}
			
			if (option instanceof SinceDateOption) {
				SinceDateOption sinceOption = (SinceDateOption)option;
				query = codesSince(sinceOption.getSince()).in(codelistId);
				codelistSizeProvider = new CodelistSizeProvider.CounterCodelistSizeProvider(repository.get(codesSince(sinceOption.getSince()).in(codelistId)));
			}
			
			if (option instanceof SessionStartedOption) {
				Date creationTime = new Date(httpSession.getCreationTime());
				query = codesSince(creationTime).in(codelistId);
				codelistSizeProvider = new CodelistSizeProvider.CounterCodelistSizeProvider(repository.get(codesSince(creationTime).in(codelistId)));
			}
			
			if (option instanceof SinceCreationOption) {
				Date creationTime = CommonDefinition.CREATED.dateOf(codelist);
				query = codesSince(creationTime).in(codelistId);
				codelistSizeProvider = new CodelistSizeProvider.CounterCodelistSizeProvider(repository.get(codesSince(creationTime).in(codelistId)));
			}

			if (query == null) throw new ServiceException("Unknown filter option "+option);

		} else {
			query = allCodesIn(codelistId);
			codelistSizeProvider = new CodelistSizeProvider.ConstantCodelistSizeProvider(codelist.codes().size());
		}

		query.from(start).to(end);

		if (sortInfo!=null) {
			if (sortInfo instanceof CodelistEditorSortInfo.CodeNameSortInfo) {
				if (sortInfo.isAscending()) query.sort(descending(byCodeName()));
				else query.sort(byCodeName());
			}
			if (sortInfo instanceof CodelistEditorSortInfo.AttributeGroupSortInfo) {
				CodelistEditorSortInfo.AttributeGroupSortInfo attributeGroupSortInfo = (CodelistEditorSortInfo.AttributeGroupSortInfo) sortInfo;
				Attribute attribute = attribute().name(ChangesetUtil.convert(attributeGroupSortInfo.getName())).value(null)
						.ofType(ChangesetUtil.convert(attributeGroupSortInfo.getType())).in(ChangesetUtil.convert(attributeGroupSortInfo.getLanguage())).build();
				if (sortInfo.isAscending()) query.sort(descending(byAttribute(attribute, attributeGroupSortInfo.getPosition() + 1)));
				else query.sort(byAttribute(attribute, attributeGroupSortInfo.getPosition() + 1));
			}
		}


		Iterable<Code> codes  = repository.get(query);
		List<UICode> uiCodes = new ArrayList<UICode>(range.getLength());
		for (Code code:codes) {
			UICode uicode = Codelists.toUiCode(code);
			uiCodes.add(uicode);
		}
		logger.trace("retrieved {} rows", uiCodes.size());
		return new DataWindow<UICode>(uiCodes, codelistSizeProvider.getSize());
	}

	private CommonDefinition[] getDefinitions(Set<String> definitionsNames) {
		CommonDefinition[] definitions = new CommonDefinition[definitionsNames.size()];
		int index = 0;
		for (String name:definitionsNames) definitions[index++] = CommonDefinition.commonDefinitionFor(name);
		return definitions;
	}

	@Override
	public UICodelistMetadata getMetadata(@Id String codelistId) throws ServiceException {
		logger.trace("getMetadata codelistId: {}", codelistId);
		Codelist codelist = repository.lookup(codelistId);
		return Codelists.toCodelistMetadata(codelist);
	}

	@Override
	@CodelistTask(LOCK)
	public AbstractFeatureCarrier.Void lock(@Id String codelistId) throws ServiceException {
		return AbstractFeatureCarrier.getVoid();
	}

	@Override
	@CodelistTask(UNLOCK)
	public AbstractFeatureCarrier.Void unlock(@Id String codelistId) throws ServiceException {
		return AbstractFeatureCarrier.getVoid();
	}

	@Override
	@CodelistTask(SEAL)
	public AbstractFeatureCarrier.Void seal(@Id String codelistId) throws ServiceException {
		return AbstractFeatureCarrier.getVoid();
	}


	@Override
	@CodelistTask(UNSEAL)
	public Void unseal(@Id String codelistId) throws ServiceException {
		return AbstractFeatureCarrier.getVoid();
	}

	@Override
	@CodelistTask(EDIT)
	public ModifyCommandResult modify(@Id String codelistId, ModifyCommand command) throws ServiceException {
		logger.trace("modify codelistId: {} command: {}", codelistId, command);
		try {
			return commandHandler.handle(codelistId, command);
		} catch(Throwable throwable)
		{
			logger.error("Error executing command "+command+" on codelist "+codelistId, throwable);
			throw new ServiceException(throwable.getMessage());
		}
	}

	@CodelistTask(VERSION)
	public UICodelistInfo createNewCodelistVersion(@Id String codelistId, String newVersion)	throws ServiceException {
		try {
			Codelist codelist = repository.lookup(codelistId);

			Codelist newCodelist = versioningService.bump(codelist).to(newVersion);

			UICodelistInfo codelistInfo = addCodelist(newCodelist);

			return codelistInfo;
		} catch(Throwable throwable)
		{
			logger.error("Error creating new codelist version for codelist "+codelistId, throwable);
			throw new ServiceException(throwable.getMessage());
		}
	}

	@Override
	@CodelistTask(VIEW)
	public ResponseWrapper<LifecycleState> getCodelistState(@Id String codelistId) throws ServiceException {
		logger.trace("getCodelistState codelistId: {}",codelistId);
		Lifecycle lifecycle = lifecycleService.lifecycleOf(codelistId);
		LifecycleState state = Codelists.getLifecycleState(lifecycle.state());
		return ResponseWrapper.wrap(state);
	}

	@Override
	public Set<UIQName> getAttributeNames(String codelistId) throws ServiceException {
		logger.trace("getAttributeNames codelistId: {}",codelistId);
		CodelistSummary summary = repository.get(summary(codelistId));

		Set<UIQName> names = new HashSet<>();
		for (QName qName:summary.allNames()) {
			names.add(ValueUtils.safeValue(qName));
		}
		return names;
	}

	@Override
	public UICodelistInfo createNewCodelist(String name, String version)	throws ServiceException {
		logger.trace("createNewCodelist name: {}, version: {}",name, version);
		Codelist newCodelist = codelist().name(name).version(version).build();

		logger.trace("owner: {}", currentUser);
		User changeset = modifyUser(currentUser).is(Roles.OWNER.on(newCodelist.id())).build();
		userRepository.update(changeset);

		UICodelistInfo codelistInfo = addCodelist(newCodelist);
		events.fire(new CodelistActionEvents.Create(newCodelist.id(),newCodelist.qname(), newCodelist.version(), session));
		return codelistInfo;
	}

	private UICodelistInfo addCodelist(Codelist newCodelist) {
		repository.add(newCodelist);

		Lifecycle lifecycle = lifecycleService.lifecycleOf(newCodelist.id());

		boolean isUserInTeam = !currentUser.fingerprint().allRolesOver(newCodelist.id(), ResourceType.codelists).isEmpty();

		UICodelistInfo codelistInfo = CodelistsInfos.toUICodelistInfo(newCodelist, lifecycle.state(), isUserInTeam);

		return codelistInfo;
	}

	@Override
	public DataWindow<UILinkDefinition> getCodelistLinkTypes(@Id String codelistId) throws ServiceException {
		logger.trace("getCodelistLinkTypes codelistId: {}", codelistId);

		Codelist codelist = repository.lookup(codelistId);

		List<UILinkDefinition> types = new ArrayList<>();
		for (LinkDefinition codelistLink:codelist.linkDefinitions()) {
			types.add(LinkDefinitions.toUILinkDefinition(codelistLink));
		}

		logger.trace("found {} link types", types.size());

		return new DataWindow<>(types);
	}

	@Override
	public List<UICodelist> getCodelists() throws ServiceException {
		logger.trace("getCodelists");
		List<UICodelist> codelists = new ArrayList<>();
		Iterable<CodelistCoordinates> result = repository.get(allListCoordinates().sort(byCoordinateName()));
		for (CodelistCoordinates codelist:result) codelists.add(Codelists.toUICodelist(codelist));
		return codelists;
	}

	@Override
	public CodelistValueTypes getCodelistValueTypes(String codelistId)	throws ServiceException {
		logger.trace("getCodelistValueTypes codelistId: {}",codelistId);
		CodelistSummary summary = repository.get(summary(codelistId));

		//FIXME namespace lost in BE
		List<AttributeValue> attributeTypes = new ArrayList<>();
		for (QName name:summary.codeNames()) {
			for (QName type:summary.codeTypesFor(name)) {
				Collection<String> languages = summary.codeLanguagesFor(name, type);
				if (languages.isEmpty()) attributeTypes.add(new AttributeValue(ValueUtils.safeValue(name), ValueUtils.safeValue(type), Language.NONE));
				else {
					for (String language:languages) {
						attributeTypes.add(new AttributeValue(ValueUtils.safeValue(name), ValueUtils.safeValue(type), ValueUtils.safeLanguage(language)));
					}
				}
			}
		}
		logger.trace("returning {} attribute types", attributeTypes.size());

		Codelist codelist = repository.lookup(codelistId);

		List<LinkValue> types = new ArrayList<>();
		for (LinkDefinition codelistLink:codelist.linkDefinitions()) {
			types.add(LinkDefinitions.toLinkValue(codelistLink));
		}

		logger.trace("returning {} link types", types.size());
		return new CodelistValueTypes(attributeTypes, types);
	}

	@Override
	public List<UILinkDefinitionInfo> getLinkTypes(String codelistId)	throws ServiceException {
		logger.trace("getLinkTypes codelistId: {}",codelistId);
		Codelist codelist = repository.lookup(codelistId);
		List<UILinkDefinitionInfo> types = new ArrayList<>();
		for (LinkDefinition link:codelist.linkDefinitions()) types.add(new UILinkDefinitionInfo(link.id(), ValueUtils.safeValue(link.qname())));
		return types;
	}

	@Override
	public List<UICodeInfo> getCodes(String codelistId, String linkTypeId) throws ServiceException {
		logger.trace("getCodes codelistId: {} linkTypeId: {}",codelistId, linkTypeId);
		Codelist codelist = repository.lookup(codelistId);
		LinkDefinition link = codelist.linkDefinitions().lookup(linkTypeId);
		Codelist target = link.target();
		List<UICodeInfo> codes = new ArrayList<>();
		for (Code code:target.codes()) codes.add(new UICodeInfo(code.id(), ValueUtils.safeValue(code.qname())));
		return codes;
	}

	@Override
	public CodelistRemoveCheckResponse canUserRemove(String codelistId) throws ServiceException {
		logger.trace("canUserDelete codelistId: {}",codelistId);

		Lifecycle lifecycle = lifecycleService.lifecycleOf(codelistId);
		if (lifecycle.state() == DefaultLifecycleStates.sealed) return CodelistRemoveCheckResponse.cannot("the codelist is sealed");

		if (!currentUser.can(CodelistAction.EDIT.on(codelistId))) return CodelistRemoveCheckResponse.cannot("insufficient premissions"); 
		return CodelistRemoveCheckResponse.can();
	}

	@CodelistTask(REMOVE)
	public void removeCodelist(@Id String codelistId) throws ServiceException {
		logger.trace("removeCodelist codelistId: {}",codelistId);
		repository.remove(codelistId);
	}

	@Override
	@CodelistTask(VIEW)
	public DataWindow<UIAttributeDefinition> getCodelistAttributeTypes(@Id String codelistId) throws ServiceException {
		logger.trace("getCodelistAttributeTypes codelistId: {}",codelistId);
		List<UIAttributeDefinition> types = new ArrayList<>();
		Codelist codelist = repository.lookup(codelistId);
		for (AttributeDefinition definition:codelist.attributeDefinitions()) types.add(AttributeDefinitions.toUIAttributeDefinition(definition));		
		return new DataWindow<UIAttributeDefinition>(types);
	}


	public String testAsync(String input) throws ServiceException {
		logger.trace("testAsync input: {}", input);
		return "This is my output "+currentUser.fullName();
	}

	@Override
	public List<UIAttributeDefinition> getMarkersAttributeTypes() throws ServiceException {
		logger.trace("getCommonAttributeTypes");
		List<UIAttributeDefinition> types = new ArrayList<>();
		for (CommonDefinition definition:CommonDefinition.markers()) types.add(AttributeDefinitions.toUIAttributeDefinition(definition.get()));		
		return types;
	}

	@Override
	@CodelistTask(VIEW)
	public List<UILogbookEntry> getLogbookEntries(@Id String codelistId) throws ServiceException {
		logger.trace("getLogbookEntries codelistId: {}", codelistId);
		Logbook logbook = logbookService.logbookOf(codelistId);
		List<UILogbookEntry> entries = LogbookEntries.toUILogbookEntries(logbook.entries());
		return entries;
	}

	@Override
	@CodelistTask(REMOVE_LOGBOOK_ENTRY)
	public void removeLogbookEntry(@Id String codelistId, String entryId) throws ServiceException {
		logger.trace("removeLogbookEntry codelistId: {}, entryId: {}", codelistId, entryId);
		Logbook logbook = logbookService.logbookOf(codelistId);
		Entry entry = logbook.find(entryId);
		logbook.remove(entry);
		logbookService.update(logbook);
	}

	@CodelistTask(EDIT)
	public void validateCodelist(@Id String codelistId) throws ServiceException {
		logger.trace("validateCodelist codelistId: {}", codelistId);
		Codelist codelist = repository.lookup(codelistId);
		validationService.validate(codelist);
	}

	@CodelistTask(EDIT)
	public void generateCodelistChangelog(@Id String codelistId) throws ServiceException {
		logger.trace("generateCodelistChangelog codelistId: {}", codelistId);
		Codelist codelist = repository.lookup(codelistId);

		//TODO: can trigger optimisation when user is allowed to say so (based on timestamps)
		boolean optimised =false;
		changelogService.track(codelist,optimised);
	}
}
