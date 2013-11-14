package org.cotrix.web.codelistmanager.server;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.repository.Queries.*;
import static org.cotrix.web.codelistmanager.shared.ManagerUIFeature.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.application.VersioningService;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Range;
import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.server.modify.ModifyCommandHandler;
import org.cotrix.web.codelistmanager.shared.CodelistGroup;
import org.cotrix.web.codelistmanager.shared.ManagerServiceException;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;
import org.cotrix.web.share.server.CotrixRemoteServlet;
import org.cotrix.web.share.server.task.ActionMapper;
import org.cotrix.web.share.server.task.CodelistTask;
import org.cotrix.web.share.server.task.ContainsTask;
import org.cotrix.web.share.server.task.Id;
import org.cotrix.web.share.server.util.CodelistLoader;
import org.cotrix.web.share.server.util.Codelists;
import org.cotrix.web.share.server.util.ValueUtils;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;
import org.cotrix.web.share.shared.codelist.UIAttribute;
import org.cotrix.web.share.shared.codelist.UICode;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
import org.cotrix.web.share.shared.feature.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The server side implementation of the RPC serialiser.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
@ContainsTask
public class ManagerServiceImpl implements ManagerService {
	
	public static class Servlet extends CotrixRemoteServlet {

		@Inject
		protected ManagerServiceImpl bean;

		@Override
		public Object getBean() {
			return bean;
		}
	}
	
	protected Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);
	
	@Inject
	ActionMapper mapper;
	
	@Inject
	CodelistRepository repository;
	
	@Inject
	protected CodelistLoader codelistLoader;
	
	@Inject
	protected ModifyCommandHandler commandHandler;
	
	@Inject
	protected VersioningService versioningService;
	
	@Inject
	protected LifecycleService lifecycleService;
	
	/** 
	 * {@inheritDoc}
	 */
	@PostConstruct
	public void init() {
		codelistLoader.importAllCodelist();
		logger.trace("codelist in repository:");
		for (Codelist codelist:repository.queryFor(allLists())) logger.trace(codelist.name().toString());
		logger.trace("done");
		
		mapper.map(VIEW).to(VIEW_CODELIST, VIEW_METADATA);
		mapper.map(EDIT).to(EDIT_METADATA, EDIT_CODELIST);
		mapper.map(LOCK).to(LOCK_CODELIST);
		mapper.map(UNLOCK).to(UNLOCK_CODELIST);
		mapper.map(SEAL).to(SEAL_CODELIST);
	}
	
	@Override
	public DataWindow<CodelistGroup> getCodelistsGrouped() throws ManagerServiceException {
		logger.trace("getCodelistsGrouped");
		
		Map<QName, CodelistGroup> groups = new HashMap<QName, CodelistGroup>();
		Iterator<org.cotrix.domain.Codelist> it = repository.queryFor(allLists()).iterator();
		while (it.hasNext()) {
			org.cotrix.domain.Codelist codelist = (org.cotrix.domain.Codelist) it.next();
			
			CodelistGroup group = groups.get(codelist.name());
			if (group == null) {
				group = new CodelistGroup(codelist.name().toString());
				groups.put(codelist.name(), group);
			}
			group.addVersion(codelist.id(), ValueUtils.safeValue(codelist.version()));
		}
		
		for (CodelistGroup group:groups.values()) Collections.sort(group.getVersions()); 
		
		return new DataWindow<CodelistGroup>(new ArrayList<CodelistGroup>(groups.values()));
	}

	@Override
	@CodelistTask(VIEW)
	public DataWindow<UICode> getCodelistCodes(@Id String codelistId, com.google.gwt.view.client.Range range) throws ManagerServiceException {
		logger.trace("getCodelistRows codelistId {}, range: {}", codelistId, range);
		
		CodelistQuery<Code> query = allCodes(codelistId);
		int from = range.getStart();
		int to = range.getStart() + range.getLength();
		logger.trace("query range from: {} to: {}", from ,to);
		query.setRange(new Range(from, to));

		Codelist codelist = repository.lookup(codelistId);
		
		Iterable<Code> codes  = repository.queryFor(query);
		List<UICode> rows = new ArrayList<UICode>(range.getLength());
		for (Code code:codes) {

			UICode uicode = new UICode();
			uicode.setId(code.id());
			uicode.setName(code.name().toString());
			
			List<UIAttribute> attributes = Codelists.toUIAttributes(code.attributes());
			uicode.setAttributes(attributes);
			rows.add(uicode);
		}
		logger.trace("retrieved {} rows", rows.size());
		return new DataWindow<UICode>(rows, codelist.codes().size());
	}

	@Override
	public UICodelistMetadata getMetadata(@Id String codelistId) throws ManagerServiceException {
		logger.trace("getMetadata codelistId: {}", codelistId);
		Codelist codelist = repository.lookup(codelistId);
		return Codelists.toCodelistMetadata(codelist);
	}

	@Override
	@CodelistTask(LOCK)
	public FeatureCarrier.Void  lock(@Id String codelistId) throws ManagerServiceException {
		return FeatureCarrier.getVoid();
	}

	@Override
	@CodelistTask(UNLOCK)
	public FeatureCarrier.Void  unlock(@Id String codelistId) throws ManagerServiceException {
		return FeatureCarrier.getVoid();
	}

	@Override
	@CodelistTask(SEAL)
	public FeatureCarrier.Void  seal(@Id String codelistId) throws ManagerServiceException {
		return FeatureCarrier.getVoid();
	}

	@Override
	@CodelistTask(EDIT)
	public ModifyCommandResult modify(@Id String codelistId, ModifyCommand command) throws ManagerServiceException {
		try {
		return commandHandler.handle(codelistId, command);
		} catch(Throwable throwable)
		{
			logger.error("Error executing command "+command+" on codelist "+codelistId, throwable);
			throw new ManagerServiceException(throwable.getMessage());
		}
	}

	@Override
	@CodelistTask(EDIT)
	public void removeCodelist(String codelistId) throws ManagerServiceException {
		logger.trace("removeCodelist codelistId: {}",codelistId);
		repository.remove(codelistId);
	}

	@Override
	@CodelistTask(EDIT)
	public CodelistGroup createNewCodelistVersion(String codelistId, String newVersion)
			throws ManagerServiceException {
		Codelist codelist = repository.lookup(codelistId);
		Codelist newCodelist = versioningService.bump(codelist).to(newVersion);
		repository.add(newCodelist);
		lifecycleService.start(newCodelist.id());
		
		CodelistGroup group = new CodelistGroup(newCodelist.name().toString());
		group.addVersion(newCodelist.id(), newCodelist.version());
		
		return group;
		
	}

	@Override
	@CodelistTask(VIEW)
	public ResponseWrapper<String> getCodelistState(@Id String codelistId) throws ManagerServiceException {
		logger.trace("getCodelistState codelistId: {}",codelistId);
		Lifecycle lifecycle = lifecycleService.lifecycleOf(codelistId);
		String state = lifecycle.state().toString();
		return ResponseWrapper.wrap(state.toUpperCase());
	}

	
}
