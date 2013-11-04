/**
 * 
 */
package org.cotrix.web.share.server.util;

import static org.cotrix.domain.dsl.Codes.*;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.io.map.MapService;
import org.cotrix.io.parse.ParseService;
import org.cotrix.io.tabular.ColumnDirectives;
import org.cotrix.io.tabular.TableMapDirectives;
import org.cotrix.io.tabular.csv.CsvParseDirectives;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.repository.CodelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistLoader {

	protected static final CodeListInfo[] codelists = new CodeListInfo[]{
		codelist("ASFIS_MINI.csv","3A_CODE", "1.0"),
		codelist("ASFIS_MINI.csv","3A_CODE", "2.0"),
		codelist("ASFIS_MINI.csv","3A_CODE", "3.0"),
		codelist("countries.csv","ISO 3166-1-alpha-2 code", "1.0")
	};

	protected Logger logger = LoggerFactory.getLogger(CodelistLoader.class);

	@Inject
	protected CodelistRepository repository;

	@Inject
	protected LifecycleService lifecycleService;

	@Inject
	protected ParseService service;

	@Inject
	protected MapService mapservice;


	public void importAllCodelist()
	{
		for (CodeListInfo codelist:codelists) {
			logger.trace("importing "+codelist);
			boolean imported = importCodelist(codelist);
			logger.trace("import "+(imported?"complete":"failed"));
		}

		importSparse();
		importComplex();
		importDemoCodelist();
	}

	public boolean importCodelist(CodeListInfo codelistInfo)
	{
		logger.trace("importCodelist resourceName: {}, codeColumnName: {}", codelistInfo.getResourceName(), codelistInfo.getCodeColumnName());
		try {
			InputStream inputStream = CodelistLoader.class.getResourceAsStream(codelistInfo.getResourceName());
			CsvParseDirectives parseDirectives = new CsvParseDirectives();
			parseDirectives.options().hasHeader(true);
			parseDirectives.options().setDelimiter('\t');
			parseDirectives.options().setQuote('"');
			Charset charset = Charset.forName("UTF-8");
			parseDirectives.options().setEncoding(charset);

			Table table = service.parse(inputStream, parseDirectives);

			List<ColumnDirectives> directives = new ArrayList<ColumnDirectives>();
			Column codeColumn = null;
			for (Column column:table.columns()) {
				if (column.name().toString().equals(codelistInfo.getCodeColumnName())) codeColumn = column;
				else directives.add(new ColumnDirectives(column));
			}
			if (codeColumn == null) throw new IllegalArgumentException("Column with name "+codelistInfo.getCodeColumnName()+" not found");

			TableMapDirectives mappingDirectives = new TableMapDirectives(codeColumn);
			mappingDirectives.name(codelistInfo.getResourceName().substring(0, codelistInfo.getResourceName().lastIndexOf('.')));
			mappingDirectives.version(codelistInfo.getVersion());

			for(ColumnDirectives directive:directives) mappingDirectives.add(directive);

			Attribute att1 = attr().name("filename").value(codelistInfo.getResourceName()).in("English").build();
			Attribute att2 = attr().name("format").value("CSV").in("English").build();

			mappingDirectives.attributes(att1, att2);

			Outcome<Codelist> outcome = mapservice.map(table, mappingDirectives);
			if (outcome.report().isFailure()) {
				logger.trace("import failed");
				return false;
			}

			Codelist codelist = outcome.result();

			repository.add(codelist);
			lifecycleService.start(codelist.id());

			return true;
		} catch(Exception e)
		{
			logger.error("Codelist import failed", e);
			return false;
		}
	}

	protected void importSparse()
	{
		Codelist codelist = createSparseCodelist(60);
		repository.add(codelist);
		lifecycleService.start(codelist.id());
	}

	protected Codelist createSparseCodelist(int ncodes)
	{
		Attribute att = attr().name("format").value("Sparse").in("English").build();

		Code[] codes = new Code[ncodes];
		for (int i = 0; i < codes.length; i++) {
			int numAttributes = i/10 + 1;
			Attribute[] attributes = new Attribute[numAttributes];
			for (int l = 0; l<attributes.length; l++) attributes[l] = attr().name("attribute"+l).value("value "+i+"-"+l).in("English").build();

			codes[i] = code().name("code"+i).attributes(attributes).build();
		}

		return Codes.codelist().name("Sparse").with(codes).attributes(att).build();
	}

	protected void importComplex()
	{
		Codelist codelist = createComplexCodelist(60);
		repository.add(codelist);
		lifecycleService.start(codelist.id());
	}

	protected Codelist createComplexCodelist(int ncodes)
	{
		Attribute[] codelistAttributes = new Attribute[3];
		codelistAttributes[0] = attr().name("format").value("complex").in("English").build();
		codelistAttributes[1] = attr().name("Author").value("Federico").in("English").build();
		codelistAttributes[2] = attr().name("Author").value("Fabio").in("English").build();

		String[] languages = new String[]{"En", "Fr", "It"};

		Code[] codes = new Code[ncodes];
		for (int i = 0; i < codes.length; i++) {
			int numAttributes = i/10 + 1;
			List<Attribute> attributes = new ArrayList<Attribute>();
			for (int l = 0; l<numAttributes; l++) {

				for (String language:languages){
					Attribute attribute = attr().name("attLang"+l).value("value "+i+"-"+l+"."+language).in(language).build();
					attributes.add(attribute);
				}

				for (int y = 0; y < 3;y++){
					Attribute attribute = attr().name("attPos"+l).value("value "+i+"-"+l+".["+y+"]").in("En").build();
					attributes.add(attribute);
				}
			}

			codes[i] = code().name("code"+i).attributes(attributes).build();
		}

		return Codes.codelist().name("Complex").with(codes).attributes(codelistAttributes).version("2.1").build();
	}
	
	protected void importDemoCodelist()
	{
		Codelist codelist = createDemoCodelist();
		repository.add(codelist);
		lifecycleService.start(codelist.id());
	}


	protected Codelist createDemoCodelist()
	{
		return Codes.codelist().name("Demo Codelist")
				.with(
						code().
						name("4060300201").
						attributes(
								attr().
								name("description").
								value("Northern elephant seal").
								ofType("description").
								in("en").build(),

								attr().
								name("description").
								value("Éléphant de mer boréal").
								ofType("description").
								in("fr").build(),
								
								attr().
								name("description").
								value("Foca elephante del norte").
								ofType("description").
								in("es").build(),
								
								attr().
								name("description").
								value("Mirounga angustirostris").
								ofType("description").
								in("la").build(),
								
								attr().
								name("description").
								value("Elefante marino settentrionale").
								ofType("description").
								in("it").build(),
								
								attr().
								name("author").
								value("Gill 1876").
								ofType("annotation").build()
								).build(),
								
								code().
								name("4060300202").
								attributes(
										attr().
										name("description").
										value("Southern elephant seal").
										ofType("description").
										in("en").build(),
										
										attr().
										name("description").
										value("Éléphant de mer austral").
										ofType("description").
										in("fr").build(),
										
										attr().
										name("description").
										value("Foca elephante del sur").
										ofType("description").
										in("es").build(),
										
										attr().
										name("description").
										value("Mirounga leonina").
										ofType("description").
										in("la").build(),
										
										attr().
										name("description").
										value("Elefante marino del Sud").
										ofType("description").
										in("it").build(),
										
										attr().
										name("author").
										value("Linnaeus 1758").
										ofType("annotation").build()
										).build(),
										
										code().
										name("4060300203").
										attributes(
												attr().
												name("description").
												value("Southern elephant").
												ofType("description").
												in("en").build(),
												
												attr().
												name("description").
												value("South elephant").
												ofType("description").
												in("en").build(),
												
												attr().
												name("description").
												value("Éléphant").
												ofType("description").
												in("fr").build(),
												
												attr().
												name("description").
												value("Éléphant").
												ofType("annotation").
												in("fr").build(),
												
												attr().
												name("author").
												value("Federico 2013").
												ofType("annotation").build()	
												,
												attr().
												name("author").
												value("Fabio 2013").
												ofType("annotation").build()
												).build()).

												attributes(
														attr().
														name("file").
														value("complex_codelist.txt").
														ofType("description").in("en").build(),
														
														attr().
														name("encoding").
														value("UTF-8").
														ofType("description").build(),
														
														attr().
														name("author").
														value("Federico").
														ofType("annotation").
														in("it").build(),
														
														attr().
														name("author").
														value("Fabio").
														ofType("annotation").
														in("it").build(),
														
														attr().
														name("author").
														value("Marco").
														ofType("description").
														in("it").build(),
														
														attr().
														name("author").
														value("Frederick").
														ofType("annotation").
														in("en").build(),
														
														attr().
														name("author").
														value("Mark").
														ofType("description").
														in("en").build()).
														version("2.2").build();
	}

	protected static CodeListInfo codelist(String resourceName, String codeColumnName, String version)
	{

		return new CodeListInfo(resourceName, codeColumnName, version);
	}

	public static class CodeListInfo {
		protected String resourceName;
		protected String codeColumnName;
		protected String version;
		/**
		 * @param resourceName
		 * @param codeColumnName
		 * @param version
		 */
		public CodeListInfo(String resourceName, String codeColumnName,
				String version) {
			this.resourceName = resourceName;
			this.codeColumnName = codeColumnName;
			this.version = version;
		}
		/**
		 * @return the resourceName
		 */
		public String getResourceName() {
			return resourceName;
		}
		/**
		 * @return the codeColumnName
		 */
		public String getCodeColumnName() {
			return codeColumnName;
		}
		/**
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("CodeListInfo [resourceName=");
			builder.append(resourceName);
			builder.append(", codeColumnName=");
			builder.append(codeColumnName);
			builder.append(", version=");
			builder.append(version);
			builder.append("]");
			return builder.toString();
		}
	}

}
