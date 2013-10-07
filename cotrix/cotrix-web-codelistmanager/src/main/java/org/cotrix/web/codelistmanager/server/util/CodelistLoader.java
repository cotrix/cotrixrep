/**
 * 
 */
package org.cotrix.web.codelistmanager.server.util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codelist;
import org.cotrix.io.map.MapService;
import org.cotrix.io.map.Outcome;
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
import static org.cotrix.domain.dsl.Codes.*;

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

			Outcome outcome = mapservice.map(table, mappingDirectives);
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
