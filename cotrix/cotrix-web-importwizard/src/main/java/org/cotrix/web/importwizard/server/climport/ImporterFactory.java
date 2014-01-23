/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.web.importwizard.server.climport.ImporterMapper.CsvMapper;
import org.cotrix.web.importwizard.server.climport.ImporterMapper.SdmxMapper;
import org.cotrix.web.importwizard.server.climport.ImporterSource.SourceParameterProvider.CodelistBeanDirectivesProvider;
import org.cotrix.web.importwizard.server.climport.ImporterSource.SourceParameterProvider.TableDirectivesProvider;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.share.shared.Progress;

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

	public Progress importCodelist(final ImportTaskSession importTaskSession, CodeListType codeListType) throws IOException
	{
		final Progress progress = new Progress();
		switch (codeListType) {
			case CSV: {
				executorService.execute(new Runnable() {

					@Override
					public void run() {
						importer.importCodelist(progress, tableDirectivesProvider, importerSource, csvMapper, importerTarget, importTaskSession);
					}
				});
			} break;
			case SDMX: {
				executorService.execute(new Runnable() {

					@Override
					public void run() {
						importer.importCodelist(progress, codelistBeanDirectivesProvider, importerSource, sdmxMapper, importerTarget, importTaskSession);
					}
				});
			} break;
		}
		return progress;
	}
}
