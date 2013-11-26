/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.io.IOException;
import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.action.events.CodelistActionEvents;
import org.cotrix.io.CloudService;
import org.cotrix.io.MapService;
import org.cotrix.io.ParseService;
import org.cotrix.io.ParseService.ParseDirectives;
import org.cotrix.io.sdmx.parse.Stream2SdmxDirectives;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.web.importwizard.server.WizardImportSession;
import org.cotrix.web.importwizard.server.util.ParsingHelper;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.Asset;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ImporterFactory {
	
	@Inject
	protected ParsingHelper parsingHelper;
	
	@Inject
	protected ParseService parseService;
	
	@Inject
	protected MapService mapservice;
	
	@Inject
	protected CodelistRepository repository;
	
	@Inject
	protected LifecycleService lifecycleService;
	
	@Inject
	protected CloudService cloud;
	
	@Inject
	private Event<CodelistActionEvents.CodelistEvent> events;
	

	public Importer<?> createImporter(WizardImportSession session, ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws IOException
	{
		switch (session.getCodeListType()) {
			case CSV: return createCsvImporter(session, metadata, mappings, mappingMode); 
			case SDMX: return createSdmxImporter(session, metadata, mappings, mappingMode);
		}
		return null;
	}

	protected Importer<Table> createCsvImporter(WizardImportSession session, ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws IOException
	{
		ImporterSource<Table> source = getCSVSource(session);
		ImporterMapper<Table> mapper = new ImporterMapper.CsvMapper(mapservice);
		Importer<Table> importer = new Importer<Table>(source, mapper, metadata, mappings, mappingMode, session);
		importer.setLifecycleService(lifecycleService);
		importer.setRepository(repository);
		importer.setEvents(events);
		return importer;
	}
	
	protected Importer<CodelistBean> createSdmxImporter(WizardImportSession session, ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws IOException
	{
		ImporterSource<CodelistBean> source = getSdmxSource(session);
		ImporterMapper<CodelistBean> mapper = new ImporterMapper.SdmxMapper(mapservice);
		Importer<CodelistBean> importer = new Importer<CodelistBean>(source, mapper, metadata, mappings, mappingMode, session);
		importer.setLifecycleService(lifecycleService);
		importer.setRepository(repository);
		importer.setEvents(events);
		return importer;
	}
	
	protected ImporterSource<Table> getCSVSource(WizardImportSession session) throws IOException
	{
		if (session.getFileField()!=null) {
			ParseDirectives<Table> parseDirectives = parsingHelper.getDirectives(session.getCsvParserConfiguration());
			ImporterSource<Table> source = new ImporterSource.ParserSource<Table>(parseService, parseDirectives, session.getFileField().getInputStream());
			return source;
		} 
		if (session.getSelectedAsset()!=null) {
			Asset asset = session.getSelectedAsset();
			ImporterSource<Table> source = new ImporterSource.RetrieveSource<Table>(cloud, asset, Table.class);
			return source;
		}

		throw new IllegalArgumentException("Both filefield and selectasset null");
	}
	
	protected ImporterSource<CodelistBean> getSdmxSource(WizardImportSession session) throws IOException
	{
		if (session.getFileField()!=null) {
			ParseDirectives<CodelistBean> parseDirectives = Stream2SdmxDirectives.DEFAULT;
			ImporterSource<CodelistBean> source = new ImporterSource.ParserSource<CodelistBean>(parseService, parseDirectives, session.getFileField().getInputStream());
			return source;
		} 
		if (session.getSelectedAsset()!=null) {
			Asset asset = session.getSelectedAsset();
			ImporterSource<CodelistBean> source = new ImporterSource.RetrieveSource<CodelistBean>(cloud, asset, CodelistBean.class);
			return source;
		}
		throw new IllegalArgumentException("Both filefield and selectasset null");
	}


}
