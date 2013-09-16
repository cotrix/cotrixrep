package org.cotrix.web.codelistmanager.server;

import java.util.ArrayList;
import java.util.Iterator;
import static org.cotrix.repository.Queries.*;
import static org.cotrix.domain.trait.Change.*;
import static org.cotrix.domain.dsl.Codes.*;
import javax.inject.Inject;
import javax.servlet.ServletException;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Range;
import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.server.util.CodelistLoader;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModel;
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
	@Override
	public void init() throws ServletException {
		super.init();
		codelistLoader.importAllCodelist();
	}
	
	public ArrayList<UICodelist> getAllCodelists()
			throws IllegalArgumentException {

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
		System.out.println("Original codelist id " + codelistId);
		Codelist c = repository.lookup(codelistId);

		Metadata meta = new Metadata();
		meta.setName(c.name().toString());
		meta.setOwner("FAO");
		meta.setRowCount(c.codes().size());
		meta.setVersion(c.version());
		meta.setDescription("This data was compiled by hand from the above and may contain errors. One small modification is that the various insular areas of the United State listed above are recorded here as the single United States Minor Outlying Islands.");

		CSVFile csvFile = new CSVFile();
		csvFile.setData(new ArrayList<String[]>());
		csvFile.setHeader(getHeader(c));

		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		model.setCsvFile(csvFile);
		model.setTotalRow(c.codes().size());
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
}
