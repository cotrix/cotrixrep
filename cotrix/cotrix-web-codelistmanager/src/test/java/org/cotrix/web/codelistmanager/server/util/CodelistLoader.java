/**
 * 
 */
package org.cotrix.web.codelistmanager.server.util;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.map.MapService;
import org.cotrix.io.map.Outcome;
import org.cotrix.io.parse.ParseService;
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
	
	protected static final String[] codelists = new String[]{"ASFIS_MINI.csv"};

	protected Logger logger = LoggerFactory.getLogger(CodelistLoader.class);

	@Inject
	protected CodelistRepository repository;

	@Inject
	protected ParseService service;

	@Inject
	protected MapService mapservice;
	
	
	public void importAllCodelist()
	{
		for (String codelist:codelists) {
			logger.trace("importing "+codelist);
			InputStream inputStream = CodelistLoader.class.getResourceAsStream(codelist);
			boolean imported = importCodelist(inputStream);
			logger.trace("import "+(imported?"complete":"failed"));
		}
	}

	public boolean importCodelist(InputStream inputStream)
	{
		try {
			CsvParseDirectives parseDirectives = new CsvParseDirectives();
			parseDirectives.options().hasHeader(true);
			parseDirectives.options().setDelimiter('\t');
			parseDirectives.options().setQuote('"');
			Charset charset = Charset.forName("UTF-8");
			parseDirectives.options().setEncoding(charset);

			Table table = service.parse(inputStream, parseDirectives);

			Column column = table.columns().get(0);
			TableMapDirectives mappingDirectives = new TableMapDirectives(column);

			Outcome outcome = mapservice.map(table, mappingDirectives);
			return !outcome.report().isFailure();
		} catch(Exception e)
		{
			logger.error("Codelist import failed", e);
			return false;
		}
	}

}