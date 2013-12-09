/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.web.importwizard.server.climport.ImporterMapper.CsvMapper;
import org.cotrix.web.importwizard.server.climport.ImporterMapper.SdmxMapper;
import org.cotrix.web.importwizard.server.climport.ImporterSource.SourceParameterProvider.CodelistBeanDirectivesProvider;
import org.cotrix.web.importwizard.server.climport.ImporterSource.SourceParameterProvider.TableDirectivesProvider;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ImporterFactory {
	
	@Inject
	protected CsvMapper csvMapper;
	
	@Inject
	protected SdmxMapper sdmxMapper;
	
	@Inject
	protected ImporterSource importerSource;
	
	@Inject
	protected TableDirectivesProvider tableDirectivesProvider;
	
	@Inject
	protected CodelistBeanDirectivesProvider codelistBeanDirectivesProvider;
	
	@Inject
	private ImporterTarget importerTarget;
	

	public Importer<?> createImporter(ImportTaskSession importTaskSession, CodeListType codeListType) throws IOException
	{

		
		switch (codeListType) {
			case CSV: return createCsvImporter(importTaskSession); 
			case SDMX: return createSdmxImporter(importTaskSession);
		}
		return null;
	}

	protected Importer<Table> createCsvImporter(ImportTaskSession importTaskSession) throws IOException
	{
		Importer<Table> importer = new Importer<Table>(tableDirectivesProvider, importerSource, csvMapper, importerTarget, importTaskSession);
		return importer;
	}
	
	protected Importer<CodelistBean> createSdmxImporter(ImportTaskSession importTaskSession) throws IOException
	{
		Importer<CodelistBean> importer = new Importer<CodelistBean>(codelistBeanDirectivesProvider, importerSource, sdmxMapper, importerTarget, importTaskSession);
		return importer;
	}
}
