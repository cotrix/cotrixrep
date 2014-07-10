/**
 * 
 */
package org.cotrix.web.ingest.server.climport;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.BeanSession;
import org.cotrix.common.events.Current;
import org.cotrix.web.ingest.server.climport.ImporterMapper.CsvMapper;
import org.cotrix.web.ingest.server.climport.ImporterMapper.SdmxMapper;
import org.cotrix.web.ingest.server.climport.ImporterSource.SourceParameterProvider.CodelistBeanDirectivesProvider;
import org.cotrix.web.ingest.server.climport.ImporterSource.SourceParameterProvider.TableDirectivesProvider;
import org.cotrix.web.ingest.shared.ImportProgress;
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

	protected ExecutorService executorService = Executors.newFixedThreadPool(10);

	@Inject
	protected Importer importer;
	
	@Inject @Current
	protected BeanSession session;

	public ImportProgress importCodelist(final ImportTaskSession importTaskSession, UIAssetType codeListType) throws IOException
	{
		final BeanSession unscopedSession = this.session.copy();
		final ImportProgress progress = new ImportProgress();
		switch (codeListType) {
			case CSV: {
				executorService.execute(new Runnable() {

					@Override
					public void run() {
						importer.importCodelist(progress, tableDirectivesProvider, importerSource, csvMapper, importerTarget, importTaskSession, unscopedSession);
					}
				});
			} break;
			case SDMX: {
				executorService.execute(new Runnable() {

					@Override
					public void run() {
						importer.importCodelist(progress, codelistBeanDirectivesProvider, importerSource, sdmxMapper, importerTarget, importTaskSession, unscopedSession);
					}
				});
			} break;
		}
		return progress;
	}
}