/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.map.MapService;
import org.cotrix.io.parse.ParseDirectives;
import org.cotrix.io.parse.ParseService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.importwizard.server.WizardImportSession;
import org.cotrix.web.importwizard.server.util.ParsingHelper;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ImporterFactory {
	
	@Inject
	ParsingHelper parsingHelper;
	
	@Inject
	ParseService parseService;
	
	@Inject
	protected MapService mapservice;
	
	@Inject
	protected CodelistRepository repository;

	public Importer<?> createImporter(WizardImportSession session, ImportMetadata metadata, List<AttributeMapping> mappings) throws IOException
	{
		switch (session.getCodeListType()) {
			case CSV: return createCSVImporter(session, metadata, mappings); 

			default:
				break;
		}
		return null;
	}

	protected Importer<Table> createCSVImporter(WizardImportSession session, ImportMetadata metadata, List<AttributeMapping> mappings) throws IOException
	{
		ImporterSource<Table> source = getCSVSource(session);
		ImporterMapper<Table> mapper = new ImporterMapper.CsvMappper(mapservice);
		Importer<Table> importer = new Importer<Table>(repository, source, mapper, mappings);
		return importer;
	}
	
	protected ImporterSource<Table> getCSVSource(WizardImportSession session) throws IOException
	{
		if (session.getFileField()!=null) {
			ParseDirectives<Table> parseDirectives = parsingHelper.getDirectives(session.getCsvParserConfiguration());
			ImporterSource<Table> source = new ImporterSource.ParserSource<Table>(parseService, parseDirectives, session.getFileField().getInputStream());
			return source;
		} 
		//TODO add asset case

		return null;
	}


}
