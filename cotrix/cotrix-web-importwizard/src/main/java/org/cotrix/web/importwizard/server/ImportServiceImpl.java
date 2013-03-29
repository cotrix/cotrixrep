package org.cotrix.web.importwizard.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.importservice.tabular.csv.CSV2Codelist;
import org.cotrix.importservice.tabular.csv.CSVOptions;
import org.cotrix.importservice.tabular.mapping.AttributeMapping;
import org.cotrix.importservice.tabular.mapping.CodelistMapping;
import org.cotrix.web.importwizard.client.CotrixModuleImport;
import org.cotrix.web.importwizard.client.ImportService;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.Metadata;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ImportServiceImpl extends RemoteServiceServlet implements ImportService {

	public boolean sendToServer(CotrixImportModel model) throws IllegalArgumentException {
		return true;
	}

	@Inject
	org.cotrix.importservice.ImportService service;

	public void testBackendConnection() throws IllegalArgumentException {
		
		String path = this.getThreadLocalRequest().getSession().getServletContext().getRealPath("files/asfis.txt");
		File file = new File(path);
		
		CotrixImportModel model = getCotrixImportModelTest();
		CSVOptions options = new CSVOptions();
		options.setDelimiter('\t');

		CodelistMapping mapping = new CodelistMapping("3A_CODE");
		QName asfisName = new QName("asfis-2012");
		mapping.setName(asfisName);
		
		HashMap<String, HeaderType> types = model.getType();
		Iterator<String> it =  types.keySet().iterator();
		List<AttributeMapping> attrs = new ArrayList<AttributeMapping>();
		while(it.hasNext()){
			String key = it.next();
			HeaderType type = types.get(key);
			
			AttributeMapping attr = new AttributeMapping (key);
			if(type.hasRelatedValue()){
				attr.setLanguage(type.getRelatedValue());
			}
			attrs.add(attr);
			
		}
		mapping.setAttributeMappings(attrs);
		CSV2Codelist directives = new CSV2Codelist(mapping, options);
		
		try {
			service.importCodelist(new FileInputStream(file), directives);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Mapping Success !!!");
	}

	private CotrixImportModel getCotrixImportModelTest(){
		final String CODE = "code";
		final String DESC = "description";

		HeaderType type3acode =  new HeaderType();
		type3acode.setValue(CODE);

		HeaderType taxocode =  new HeaderType();
		taxocode.setValue(CODE);

		HeaderType isscapp =  new HeaderType();
		isscapp.setValue(CODE);

		HeaderType scientificName =  new HeaderType();
		scientificName.setValue(DESC);

		HeaderType englishName =  new HeaderType();
		englishName.setValue(DESC);
		englishName.setRelatedValue("en");

		HeaderType spanishName =  new HeaderType();
		spanishName.setValue(DESC);
		spanishName.setRelatedValue("es");

		HeaderType frenchName =  new HeaderType();
		frenchName.setValue(DESC);
		frenchName.setRelatedValue("fr");

		HeaderType author =  new HeaderType();
		author.setValue(CODE);

		HeaderType family =  new HeaderType();
		family.setValue(CODE);

		HeaderType order =  new HeaderType();
		order.setValue(CODE);


		HashMap<String, HeaderType> types = new HashMap<String, HeaderType>();
		types.put("3A_CODE",type3acode);
		types.put("TAXOCODE",taxocode);
		types.put("ISSCAAP",isscapp);
		types.put("Scientific_name",scientificName);
		types.put("English_name",englishName);
		types.put("Spanish_name",spanishName);
		types.put("Author",author);
		types.put("Family",family);
		types.put("Order",order);


		CotrixImportModel model = new CotrixImportModel();
		model.setType(types);
		
		return model;
	}

}
