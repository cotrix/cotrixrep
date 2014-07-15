/**
 * 
 */
package org.cotrix.web.ingest.server.climport;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.web.ingest.server.climport.ImporterMapper.CsvMapper;
import org.cotrix.web.ingest.server.climport.ImporterMapper.SdmxMapper;
import org.cotrix.web.ingest.server.climport.ImporterSource.SourceParameterProvider.CodelistBeanDirectivesProvider;
import org.cotrix.web.ingest.server.climport.ImporterSource.SourceParameterProvider.TableDirectivesProvider;
import org.cotrix.web.ingest.shared.ImportResult;
import org.cotrix.web.ingest.shared.UIAssetType;

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

	@Inject
	protected Importer importer;

	public ImportResult importCodelist(final ImportTaskSession importTaskSession, UIAssetType codeListType) throws Exception
	{
		switch (codeListType) {
			case CSV: return importer.importCodelist(tableDirectivesProvider, importerSource, csvMapper, importerTarget, importTaskSession);
			case SDMX: return importer.importCodelist(codelistBeanDirectivesProvider, importerSource, sdmxMapper, importerTarget, importTaskSession);
			default: return null;
		}
	}
}
