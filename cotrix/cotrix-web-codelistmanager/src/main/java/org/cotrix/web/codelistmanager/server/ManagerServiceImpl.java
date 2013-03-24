package org.cotrix.web.codelistmanager.server;

import java.util.ArrayList;

import org.cotrix.web.codelistmanager.client.CotrixModuleManager;
import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.shared.FieldVerifier;
import org.cotrix.web.share.shared.Codelist;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {
	public ArrayList<Codelist> getAllCodelists() throws IllegalArgumentException {
		Codelist c1 = new Codelist();
		c1.setName("Country");
		c1.setId(0);
		
		Codelist c2 = new Codelist();
		c2.setName("Continent");
		c2.setId(1);

		Codelist c3 = new Codelist();
		c3.setName("ASFIS");
		c3.setId(2);

		Codelist c4 = new Codelist();
		c4.setName("Language");
		c4.setId(3);

		Codelist c5 = new Codelist();
		c5.setName("Year");
		c5.setId(4);

		Codelist c6 = new Codelist();
		c6.setName("Month");
		c6.setId(5);
		
		ArrayList<Codelist> list = new ArrayList<Codelist>();
		list.add(c1);
		list.add(c2);
		list.add(c3);
		list.add(c4);
		list.add(c5);
		list.add(c6);
		
		return list;
	}
	
	public CotrixImportModel getCodeListModel(int codelistId) {
		CotrixImportModel model = null;
		switch (codelistId) {
		case 0:
			model = getCountry();
			break;
		case 1:
			model = getContinent();
			break;
		case 2:
			model = getASFIS();
			break;
		case 3:
			model = getLanguage();
			break;
		case 4:
			model = getYear();
			break;
		case 5:
			model = getMonth();
			break;

		default:
			break;
		}

		
		return model;
	}
	
	private CotrixImportModel getMonth(){
		Metadata meta = new Metadata();
		meta.setName("Month");
		meta.setOwner("FAO");
		meta.setVersion("1.9");
		meta.setDescription("Financial contracts traded on US futures exchanges (such as bonds, short term interest rates, foreign exchange and US stock indexes) tend to expire quarterly, in March, June, September and December. For financial contracts traded on non-US futures exchanges, the expiration schedule may not be quarterly.");
		
		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		return model;
	}
	private CotrixImportModel getYear(){
		Metadata meta = new Metadata();
		meta.setName("Year");
		meta.setOwner("FAO");
		meta.setVersion("0.9");
		meta.setDescription("Detailed codes for variables that contain a large number of coded responses, such as ancestry and occupation");
		
		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		return model;
	}
	private CotrixImportModel getLanguage(){
		Metadata meta = new Metadata();
		meta.setName("Language");
		meta.setOwner("FAO");
		meta.setVersion("0.9");
		meta.setDescription("ISO 639-1 defines abbreviations for languages. In HTML and XHTML they can be used in the lang and xml:lang attributes");
		
		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		return model;
	}
	private CotrixImportModel getContinent(){
		Metadata meta = new Metadata();
		meta.setName("Continent");
		meta.setOwner("FAO");
		meta.setVersion("0.9");
		meta.setDescription("This data was compiled by hand from the above and may contain errors. One small modification is that the various insular areas of the United State listed above are recorded here as the single United States Minor Outlying Islands.");
		
		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		return model;
	}
	private CotrixImportModel getCountry(){
		Metadata meta = new Metadata();
		meta.setName("Country");
		meta.setOwner("FAO");
		meta.setVersion("0.9");
		meta.setDescription("CountryCode.org is your complete guide to call anywhere in the world. The calling chart below will help you find the dialing codes you need to make long distance phone calls to friends, family, and business partners around the globe. Simply find and click the country you wish to call. You'll find instructions on how to call that country using its country code, as well as other helpful information like area codes, ISO country codes, and the kinds of electrical outlets and phone jacks found in that part of the world. Making a phone call has never been easier with CountryCode.org.");
		
		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		return model;
	}
	private CotrixImportModel getASFIS(){
		Metadata meta = new Metadata();
		meta.setName("ASFIS");
		meta.setOwner("FAO");
		meta.setVersion("0.9");
		meta.setDescription("ASFIS list of species includes 12 000 species items selected according to their interest or relation to fisheries and aquaculture. For each species item stored in a record, codes (ISSCAAP group, taxonomic and 3-alpha) and taxonomic information (scientific name, author(s), family, and higher taxonomic classification) are provided. An English name is available for most of the records, and about one third of them have also a French and Spanish name. Information is also provided about the availability of fishery production statistics on the species item in the FAO databases.");
		
		CotrixImportModel model = new CotrixImportModel();
		model.setMetadata(meta);
		return model;
	}
}
