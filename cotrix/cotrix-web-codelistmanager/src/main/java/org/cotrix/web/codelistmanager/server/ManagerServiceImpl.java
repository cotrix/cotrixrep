package org.cotrix.web.codelistmanager.server;

import static org.cotrix.repository.Queries.allCodes;
import static org.cotrix.repository.Queries.allLists;

import java.util.ArrayList;
import java.util.Iterator;

import javax.inject.Inject;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Range;
import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.Codelist;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {
	@Inject CodelistRepository repository;

	public ArrayList<Codelist> getAllCodelists()throws IllegalArgumentException {
		ArrayList<Codelist> list = new ArrayList<Codelist>();
		/*Iterator<org.cotrix.domain.Codelist> it  = repository.queryFor(allLists()).iterator();
		while (it.hasNext()) {
			org.cotrix.domain.Codelist codelist = (org.cotrix.domain.Codelist) it.next();
			Codelist c = new Codelist();
			c.setName(codelist.name().toString());
			c.setId(codelist.id());
			list.add(c);
		}*/

		// Just for testing perpose.
		Codelist c = new Codelist();
		c.setName("ASFIS");
		c.setId("1");
		list.add(c);
		return list;
	}

	public CotrixImportModel getCodeListModel(String codelistId) {


		/*org.cotrix.domain.Codelist c =  repository.lookup(codelistId);

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


		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		model.setCsvFile(csvFile);
		return model;*/
		return getASFIS();
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

	/*public ArrayList<String[]> getDataRange(String id, int start, int end) {
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
	}*/
	private CotrixImportModel getASFIS(){
		Metadata meta = new Metadata();
		meta.setName("ASFIS");
		meta.setOwner("FAO");
		meta.setVersion("0.9");
		meta.setDescription("ASFIS list of species includes 12 000 species items selected according to their interest or relation to fisheries and aquaculture. For each species item stored in a record, codes (ISSCAAP group, taxonomic and 3-alpha) and taxonomic information (scientific name, author(s), family, and higher taxonomic classification) are provided. An English name is available for most of the records, and about one third of them have also a French and Spanish name. Information is also provided about the availability of fishery production statistics on the species item in the FAO databases.");

		ArrayList<String[]> line = Util.readFile(this.getThreadLocalRequest().getSession().getServletContext().getRealPath("files/ASFIS_sp_Feb_2012.txt"),"\t");
		CSVFile csvFile = new CSVFile();
		csvFile.setData(new ArrayList<String[]>(line.subList(1, line.size())));
		csvFile.setHeader(line.get(0));

		
		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		model.setCsvFile(csvFile);
		return model;
	}

	public ArrayList<String[]> getDataRange(String id, int start, int end) {
		ArrayList<String[]> data = new ArrayList<String[]>();
		ArrayList<String[]> line = Util.readFile(this.getThreadLocalRequest().getSession().getServletContext().getRealPath("files/ASFIS_sp_Feb_2012.txt"),"\t");
		data = new ArrayList<String[]>(line.subList(1, line.size()));

		if(data.size() < end){
			end = data.size();
		}
		return new ArrayList<String[]>(data.subList(start, end));
	}


}
