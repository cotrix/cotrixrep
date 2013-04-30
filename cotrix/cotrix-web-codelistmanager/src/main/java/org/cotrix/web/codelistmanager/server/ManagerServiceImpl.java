package org.cotrix.web.codelistmanager.server;

import static org.cotrix.domain.dsl.Codes.codelist;
import static org.cotrix.repository.Queries.allCodes;
import static org.cotrix.repository.Queries.allLists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.swing.text.StyledEditorKit.ItalicAction;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Range;
import org.cotrix.web.codelistmanager.client.CotrixModuleManager;
import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.client.presenter.CodeListDetailPresenter;
import org.cotrix.web.codelistmanager.shared.FieldVerifier;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.Codelist;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {
@Inject CodelistRepository repository;
	
	public ArrayList<Codelist> getAllCodelists()throws IllegalArgumentException {
		ArrayList<Codelist> list = new ArrayList<Codelist>();
		Iterator<org.cotrix.domain.Codelist> it  = repository.queryFor(allLists()).iterator();
		while (it.hasNext()) {
			org.cotrix.domain.Codelist codelist = (org.cotrix.domain.Codelist) it.next();
			Codelist c = new Codelist();
			c.setName(codelist.name().toString());
			c.setId(codelist.id());
			list.add(c);
		}
		
		return list;
	}
	
	public CotrixImportModel getCodeListModel(String codelistId) {
		
		
		org.cotrix.domain.Codelist c =  repository.lookup(codelistId);
		
		Metadata meta = new Metadata();
		meta.setName(c.name().toString());
		meta.setOwner("FAO");
		meta.setRowCount(c.codes().size());
		meta.setVersion(c.version());
		meta.setDescription("This data was compiled by hand from the above and may contain errors. One small modification is that the various insular areas of the United State listed above are recorded here as the single United States Minor Outlying Islands.");
		
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		CSVFile csvFile = new CSVFile();
		csvFile.setData(data);
		csvFile.setHeader(getHeader(c));
		csvFile.setFilename("xxxx.txt");
		
		
		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		model.setCsvFile(csvFile);
		return model;
	}
	
	private String[] getHeader(org.cotrix.domain.Codelist codelist){
		CodelistQuery<Code> codes = allCodes(codelist.id());
		codes.setRange(new Range(0,1));
		String[] line = null;
		Iterable<Code> inrange  = repository.queryFor(codes);
		
		Iterator<Code> it = inrange.iterator();
		while (it.hasNext()) {
			Code code = (Code)  it.next();
			line = new String[code.attributes().size()];
			Iterator it2 =  code.attributes().iterator();
			int index = 0 ;
			while (it2.hasNext()) {
				Attribute a = (Attribute) it2.next();
				line[index++] = a.name().toString();
			}
		}
		return line;
	}
	
	public ArrayList<String[]> getDataRange(String id, int start, int end) {
		ArrayList<String[]> data = new ArrayList<String[]>();
		CodelistQuery<Code> codes = allCodes(id);
		codes.setRange(new Range(start,end));
		
		Iterable<Code> inrange  = repository.queryFor(codes);
		
		Iterator<Code> it = inrange.iterator();
		while (it.hasNext()) {
			Code code = (Code)  it.next();
			String[] line = new String[code.attributes().size()];
			Iterator it2 =  code.attributes().iterator();
			int index = 0 ;
			while (it2.hasNext()) {
				Attribute a = (Attribute) it2.next();
				line[index++] = a.value();
			}
			data.add(line);	
		}
		
		return data;
	}

	
}
