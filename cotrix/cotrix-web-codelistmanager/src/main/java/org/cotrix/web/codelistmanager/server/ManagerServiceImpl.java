package org.cotrix.web.codelistmanager.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.cotrix.repository.Queries.*;
import static org.cotrix.domain.trait.Change.*;
import static org.cotrix.domain.dsl.Codes.*;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.Container;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Range;
import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.server.util.CodelistLoader;
import org.cotrix.web.codelistmanager.shared.CodeListAttribute;
import org.cotrix.web.codelistmanager.shared.CodeListMetadata;
import org.cotrix.web.codelistmanager.shared.ManagerServiceException;
import org.cotrix.web.codelistmanager.shared.UICodeListRow;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.UIAttribute;
import org.cotrix.web.share.shared.UICode;
import org.cotrix.web.share.shared.UICodelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {
	
	protected Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);
	
	@Inject
	CodelistRepository repository;
	
	@Inject
	protected CodelistLoader codelistLoader;
	
	/** 
	 * {@inheritDoc}
	 */
	public void init() throws ServletException {
		super.init();
		codelistLoader.importAllCodelist();
		logger.trace("codelist in repository:");
		for (Codelist codelist:repository.queryFor(allLists())) logger.trace(codelist.name().toString());
		logger.trace("done");
	}
	
	public ArrayList<UICodelist> getAllCodelists() throws IllegalArgumentException {

		ArrayList<UICodelist> list = new ArrayList<UICodelist>();
		Iterator<org.cotrix.domain.Codelist> it = repository.queryFor(allLists()).iterator();
		while (it.hasNext()) {
			org.cotrix.domain.Codelist codelist = (org.cotrix.domain.Codelist) it
					.next();
			UICodelist c = new UICodelist();
			c.setName(codelist.name().toString());
			c.setId(codelist.id());
			list.add(c);
		}
		return list;
	}

	public void addCode(ArrayList<UICode> codes){
		Attribute.Private a = (Attribute.Private) attr().name("TAXOCODE").value("OOOOOOO").as(NEW).build();
		Code.Private code = (Code.Private) code().name("LAU").attributes(a).as(NEW).build();
		Codelist changeset = codelist("id").name("sss").with(code).as(NEW).build();

	}

	public void editCode(ArrayList<UICode> editedCodes) {
		for (UICode code : editedCodes) {
			Attribute changedAttribute = attr(code.getAttribute().getId()).name(code.getAttribute().getName()).value(code.getAttribute().getValue()).as(MODIFIED).build();
			Code changeCode =  code(code.getId()).name(code.getName()).attributes(changedAttribute).as(MODIFIED).build();
			Codelist changeset = codelist(code.getParent().getId()).name(code.getParent().getName()).with(changeCode).as(MODIFIED).build();
			repository.update(changeset);
		}
		/*
		Attribute a =  attr().name(code.getAttribute().getName()).value(code.getAttribute().getValue()).as(NEW).build();
		Code c = code().name(code.getName()).attributes(a).as(NEW).build();
		Codelist codelist = codelist(code.getParent().getId()).name(code.getParent().getName()).with(c).as(NEW).build();*/
	}

	public CotrixImportModel getCodeListModel(String codelistId) {
		logger.trace("getCodeListModel codelistId {} ", codelistId);
		
		Codelist codelist = repository.lookup(codelistId);
		logger.trace("lookup found {}", codelist.name().toString());
		logger.trace("codelist size {}", codelist.codes().size());
		

		Metadata meta = new Metadata();
		meta.setName(codelist.name().toString());
		meta.setOwner("FAO");
		meta.setRowCount(codelist.codes().size());
		meta.setVersion(codelist.version());
		meta.setDescription("This data was compiled by hand from the above and may contain errors. One small modification is that the various insular areas of the United State listed above are recorded here as the single United States Minor Outlying Islands.");

		CSVFile csvFile = new CSVFile();
		csvFile.setData(new ArrayList<String[]>());
		csvFile.setHeader(getHeader(codelist));

		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		model.setCsvFile(csvFile);
		model.setTotalRow(codelist.codes().size());
		return model;
	}

	private String[] getHeader(Codelist codelist) {
		CodelistQuery<Code> codes = allCodes(codelist.id());
		codes.setRange(new Range(0, 1));
		String[] line = null;
		Iterable<Code> inrange = repository.queryFor(codes);

		Iterator<Code> it = inrange.iterator();
		while (it.hasNext()) {
			Code code = (Code) it.next();
			line = new String[code.attributes().size()];
			Iterator it2 = code.attributes().iterator();
			int index = 0;
			while (it2.hasNext()) {
				Attribute a = (Attribute) it2.next();
				line[index++] = a.name().toString();
			}
		}
		return line;
	}

	public ArrayList<UICode[]> getDataRange(String id, int start, int end) {
		logger.trace("getDataRange id {}, start: {}, end: {}", id, start, end);
		
		
		ArrayList<UICode[]> data = new ArrayList<UICode[]>();
		CodelistQuery<Code> codes = allCodes(id);
		codes.setRange(new Range(start,end));

		Codelist codelist = repository.lookup(id);
		Iterable<Code> inrange  = repository.queryFor(codes);
		Iterator<Code> it = inrange.iterator();
		while (it.hasNext()) {
			Code code = (Code)  it.next();
			System.out.println("attribute size = "+code.attributes().size());
			UICode[] line = new UICode[code.attributes().size()];
			Iterator<Attribute> it2 =  (Iterator<Attribute>) code.attributes().iterator();
			int index = 0 ;
			while (it2.hasNext()) {
				Attribute a = (Attribute) it2.next();

				UIAttribute uiAttr = new UIAttribute();
				uiAttr.setName(a.name().toString());
				uiAttr.setType(a.type().toString());
				uiAttr.setLanguage(a.language());
				uiAttr.setValue(a.value());
				uiAttr.setId(a.id());

				UICodelist uiCodeList = new UICodelist();
				uiCodeList.setId(codelist.id());
				uiCodeList.setName(codelist.name().toString());

				UICode uiCode = new UICode();
				uiCode.setAttribute(uiAttr);
				uiCode.setId(code.id());
				uiCode.setName(code.name().toString());
				uiCode.setParent(uiCodeList);

				line[index++] = uiCode;

			}
			data.add(line);	
		}
		return data;
	}

	@Override
	public DataWindow<UICodelist> getCodelists(com.google.gwt.view.client.Range range) throws ManagerServiceException {
		logger.trace("getCodelists range: {}", range);
		ArrayList<UICodelist> list = new ArrayList<UICodelist>();
		Iterator<org.cotrix.domain.Codelist> it = repository.queryFor(allLists()).iterator();
		while (it.hasNext()) {
			org.cotrix.domain.Codelist codelist = (org.cotrix.domain.Codelist) it
					.next();
			UICodelist c = new UICodelist();
			c.setName(codelist.name().toString());
			c.setId(codelist.id());
			list.add(c);
		}
		return new DataWindow<UICodelist>(list);
	}

	@Override
	public DataWindow<UICodeListRow> getCodelistRows(String codelistId, com.google.gwt.view.client.Range range) throws ManagerServiceException {
		logger.trace("getCodelistRows codelistId {}, range: {}", codelistId, range);
		
		CodelistQuery<Code> query = allCodes(codelistId);
		query.setRange(new Range(range.getStart(), range.getStart() + range.getLength()));

		Codelist codelist = repository.lookup(codelistId);
		
		Iterable<Code> codes  = repository.queryFor(query);
		List<UICodeListRow> rows = new ArrayList<UICodeListRow>(range.getLength());
		for (Code code:codes) {

			UICodeListRow row = new UICodeListRow(code.id(), code.id(), code.name().toString());
			
			Map<String, UIAttribute> rowAttributes = new HashMap<String, UIAttribute>(code.attributes().size());
			
			for (Attribute attribute:code.attributes()) {
				UIAttribute rowAttribute = new UIAttribute();
				rowAttribute.setName(attribute.name().toString());
				rowAttribute.setType(attribute.type().toString());
				rowAttribute.setLanguage(attribute.language());
				rowAttribute.setValue(attribute.value());
				rowAttribute.setId(attribute.id());
				rowAttributes.put(attribute.name().toString(), rowAttribute);
			}
			row.setAttributes(rowAttributes);
			rows.add(row);
		}
		return new DataWindow<UICodeListRow>(rows, codelist.codes().size());
	}

	@Override
	public CodeListMetadata getMetadata(String codelistId) throws ManagerServiceException {
		logger.trace("getMetadata codelistId: {}", codelistId);
		Codelist codelist = repository.lookup(codelistId);
		CodeListMetadata metadata = new CodeListMetadata();
		metadata.setId(codelist.id());
		metadata.setName(codelist.name().toString());
		metadata.setVersion(codelist.version());
		metadata.setAttributes(getAttributes(codelist.attributes()));
		return metadata;
	}
	
	protected List<CodeListAttribute> getAttributes(Container<? extends Attribute> attributesContainer)
	{
		if (attributesContainer.size()==0) return Collections.emptyList();
		List<CodeListAttribute> attributes = new ArrayList<CodeListAttribute>(attributesContainer.size());
		
		for (Attribute domainAttribute:attributesContainer) {
			CodeListAttribute attribute = new CodeListAttribute();
			attribute.setId(domainAttribute.id());
			attribute.setName(domainAttribute.name().toString());
			attribute.setType(domainAttribute.type().toString());
			attribute.setLanguage(domainAttribute.language());
			attribute.setValue(domainAttribute.value());
			attributes.add(attribute);
		}
		
		return attributes;
	}

	@Override
	public void saveMetadata(String codelistId, CodeListMetadata metadata) throws ManagerServiceException {
		logger.trace("saveMetadata codelistId: {}, metadata {}", codelistId, metadata);
		Codelist changeset = codelist(codelistId).name(metadata.getName()).version(metadata.getVersion()).as(MODIFIED).build();
		//TODO attributes?
		repository.update(changeset);
	}
}
