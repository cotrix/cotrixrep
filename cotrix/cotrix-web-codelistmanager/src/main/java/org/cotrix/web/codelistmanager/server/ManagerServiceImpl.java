package org.cotrix.web.codelistmanager.server;



import static org.cotrix.repository.Queries.allCodes;
import static org.cotrix.repository.Queries.allLists;
import static org.cotrix.domain.trait.Change.*;
import static org.cotrix.domain.utils.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistStartClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.importservice.ImportService;
import org.cotrix.importservice.Outcome;
import org.cotrix.importservice.tabular.csv.CSV2Codelist;
import org.cotrix.importservice.tabular.csv.CSVOptions;
import org.cotrix.importservice.tabular.mapping.AttributeMapping;
import org.cotrix.importservice.tabular.mapping.CodelistMapping;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Range;
import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.shared.CodeCell;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.UICodelist;
import org.omg.IOP.Codec;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {
	@Inject CodelistRepository repository;
	@Inject ImportService service;
	
	public ArrayList<UICodelist> getAllCodelists()throws IllegalArgumentException {
		// Preloaded codelists for demo purpose.
		loadASFIS();
		
		
		ArrayList<UICodelist> list = new ArrayList<UICodelist>();
		Iterator<org.cotrix.domain.Codelist> it  = repository.queryFor(allLists()).iterator();
		while (it.hasNext()) {
			org.cotrix.domain.Codelist codelist = (org.cotrix.domain.Codelist) it.next();
			UICodelist c = new UICodelist();
			c.setName(codelist.name().toString());
			c.setId(codelist.id());
			list.add(c);
		}

		return list;
	}
	
	public void editCode(CodeCell codeCell){
		
		System.out.println("Edit codelist id "+codeCell.getCodelistId());
		try {
			System.out.println("Codelist name is "+codeCell.getCodelistName());
			System.out.println("Code id is "+codeCell.getId());
			System.out.println("Code name is "+codeCell.getName());
			
			Codelist changeset = codelist(codeCell.getCodelistId()).name(codeCell.getCodelistName()).with(
					code(codeCell.getId()).name(codeCell.getName()).as(MODIFIED).build()
					)
					.build(); 
			System.out.println("I have got changeset");
			repository.update(changeset);
			System.out.println("I can update codelist");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CotrixImportModel getCodeListModel(String codelistId) {
		System.out.println("Original codelist id "+codelistId);
		Codelist c =  repository.lookup(codelistId);

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

	private String[] getHeader(Codelist codelist){
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
	
	public ArrayList<CodeCell[]> getDataRange(String id, int start, int end) {
		System.out.println("Fetch 30 first rows of codelist"+id);
		ArrayList<CodeCell[]> data = new ArrayList<CodeCell[]>();
		CodelistQuery<Code> codes = allCodes(id);
		codes.setRange(new Range(start,end));

		Iterable<Code> inrange  = repository.queryFor(codes);

		Iterator<Code> it = inrange.iterator();
		while (it.hasNext()) {
			Code code = (Code)  it.next();
			CodeCell[] line = new CodeCell[code.attributes().size()];
			Iterator it2 =  code.attributes().iterator();
			int index = 0 ;
			while (it2.hasNext()) {
				Attribute a = (Attribute) it2.next();
				
				CodeCell c = new CodeCell();
				c.setId(a.id());
				c.setCodelistId(id);
				c.setCodelistName(repository.lookup(id).name().toString());
				c.setLanguage(a.language());
				c.setName(a.name().toString());
				c.setType(a.type().toString());
				c.setValue(a.value());
				
				line[index++] = c;
			}
			data.add(line);	
		}

		return data;
	}
	
	private void loadASFIS(){
		FileInputStream is =  Util.readFile(this.getThreadLocalRequest().getSession().getServletContext().getRealPath("files/ASFIS_sp_Feb_2012.txt"));
		
		List<String> headers = new ArrayList<String>();
		headers.add("ISSCAAP");
		headers.add("TAXOCODE");
		headers.add("3A_CODE");
		headers.add("Scientific_name");
		headers.add("English_name");
		headers.add("French_name");
		headers.add("Spanish_name");
		headers.add("Author");
		headers.add("Family");
		headers.add("Order");
		headers.add("Stats_data");
		
		Outcome<Codelist> outcome = save(headers,is);
		outcome.result();
		
		System.out.println(outcome.report());
		
	}
	private Outcome<Codelist> save(List<String> types,InputStream stream){
		CSVOptions options = new CSVOptions();
		options.setDelimiter('\t');
		options.setColumns(types,true);
		
		CodelistMapping mapping = new CodelistMapping("3A_CODE");
		QName asfisName = new QName("asfis-2012");
		mapping.setName(asfisName);

		List<AttributeMapping> attrs = new ArrayList<AttributeMapping>();
		for (String type : types) {
			AttributeMapping attr = new AttributeMapping (type.trim());
			attrs.add(attr);
		}
		mapping.setAttributeMappings(attrs);

		CSV2Codelist directives = new CSV2Codelist(mapping, options);
		return service.importCodelist(stream, directives);
	}
	


}
