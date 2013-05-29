package org.cotrix.web.publish.server;



import static org.cotrix.repository.Queries.allCodes;
import static org.cotrix.repository.Queries.allLists;

import java.util.ArrayList;
import java.util.Iterator;

import javax.inject.Inject;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.io.Channels;
import org.cotrix.io.PublicationService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Range;
import org.cotrix.web.publish.client.PublishService;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PublishServiceImpl extends RemoteServiceServlet implements
		PublishService {

	@Inject CodelistRepository repository;
	@Inject PublicationService service;
	@Inject Channels channels;
	
	public ArrayList<UICodelist> getAllCodelists()throws IllegalArgumentException {
		ArrayList<UICodelist> list = new ArrayList<UICodelist>();
		Iterator<Codelist> it  = repository.queryFor(allLists()).iterator();
		while (it.hasNext()) {
			Codelist codelist = (Codelist) it.next();
			UICodelist c = new UICodelist();
			c.setName(codelist.name().toString());
			c.setId(codelist.id());
			list.add(c);
		}
		return list;
	}
	
	public void getAllChanels(){
		channels.publicationTypes
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
		return model;
	}
}
