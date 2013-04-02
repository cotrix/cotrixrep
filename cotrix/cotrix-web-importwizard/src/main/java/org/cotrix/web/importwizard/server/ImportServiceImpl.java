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
import org.cotrix.web.share.shared.HeaderType;
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
		
		ArrayList<HeaderType> types = model.getType();
		List<AttributeMapping> attrs = new ArrayList<AttributeMapping>();
		for (HeaderType type : types) {
			AttributeMapping attr = new AttributeMapping (type.getName());
			if(type.getRelatedValue()!=null){
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
		type3acode.setName("3A_CODE");
		

		HeaderType taxocode =  new HeaderType();
		taxocode.setValue(CODE);
		taxocode.setName("TAXOCODE");

		HeaderType isscapp =  new HeaderType();
		isscapp.setValue(CODE);
		isscapp.setName("ISSCAAP");

		HeaderType scientificName =  new HeaderType();
		scientificName.setValue(DESC);
		scientificName.setName("Scientific_name");

		HeaderType englishName =  new HeaderType();
		englishName.setValue(DESC);
		englishName.setRelatedValue("en");
		englishName.setName("English_name");

		HeaderType spanishName =  new HeaderType();
		spanishName.setValue(DESC);
		spanishName.setRelatedValue("es");
		spanishName.setName("Spanish_name");

		HeaderType frenchName =  new HeaderType();
		frenchName.setValue(DESC);
		frenchName.setRelatedValue("fr");
		frenchName.setName("French_name");

		HeaderType author =  new HeaderType();
		author.setValue(CODE);
		author.setName("Family");

		HeaderType family =  new HeaderType();
		family.setValue(CODE);
		family.setName("Author");

		HeaderType order =  new HeaderType();
		order.setValue(CODE);
		order.setName("Order");

		ArrayList<HeaderType> types = new ArrayList<HeaderType>();


		CotrixImportModel model = new CotrixImportModel();
		model.setType(types);
		
		return model;
	}

}
