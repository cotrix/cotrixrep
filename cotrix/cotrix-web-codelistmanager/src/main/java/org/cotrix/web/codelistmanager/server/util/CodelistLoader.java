/**
 * 
 */
package org.cotrix.web.codelistmanager.server.util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.map.MapService;
import org.cotrix.io.map.Outcome;
import org.cotrix.io.parse.ParseService;
import org.cotrix.io.tabular.ColumnDirectives;
import org.cotrix.io.tabular.TableMapDirectives;
import org.cotrix.io.tabular.csv.CsvParseDirectives;
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
	
	protected static final String[][] codelists = new String[][]{{"ASFIS_MINI.csv","3A_CODE"},{"countries.csv","ISO 3166-1-alpha-2 code"}};

	protected Logger logger = LoggerFactory.getLogger(CodelistLoader.class);

	@Inject
	protected CodelistRepository repository;

	@Inject
	protected ParseService service;

	@Inject
	protected MapService mapservice;
	
	
	public void importAllCodelist()
	{
		for (String[] codelist:codelists) {
			logger.trace("importing "+Arrays.toString(codelist));
			boolean imported = importCodelist(codelist);
			logger.trace("import "+(imported?"complete":"failed"));
		}
	}

	public boolean importCodelist(String[] codelist)
	{
		String resourceName = codelist[0];
		String codeColumnName = codelist[1];
		logger.trace("importCodelist resourceName: {}, codeColumnName: {}", resourceName, codeColumnName);
		try {
			InputStream inputStream = CodelistLoader.class.getResourceAsStream(resourceName);
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
				if (column.name().toString().equals(codeColumnName)) codeColumn = column;
				else directives.add(new ColumnDirectives(column));
			}
			if (codeColumn == null) throw new IllegalArgumentException("Column with name "+codeColumnName+" not found");

			TableMapDirectives mappingDirectives = new TableMapDirectives(codeColumn);
			mappingDirectives.name(resourceName.substring(0, resourceName.lastIndexOf('.')));
			
			for(ColumnDirectives directive:directives) mappingDirectives.add(directive);

			Outcome outcome = mapservice.map(table, mappingDirectives);
			if (outcome.report().isFailure()) {
				logger.trace("import failed");
				return false;
			}
			
			repository.add(outcome.result());
			return true;
		} catch(Exception e)
		{
			logger.error("Codelist import failed", e);
			return false;
		}
	}

}
