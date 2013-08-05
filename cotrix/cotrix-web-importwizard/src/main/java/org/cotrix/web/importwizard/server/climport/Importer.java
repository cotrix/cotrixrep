/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import org.cotrix.io.map.Outcome;
import org.cotrix.io.map.Report;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.importwizard.shared.AttributesMappings;
import org.cotrix.web.importwizard.shared.ImportProgress;
import org.cotrix.web.importwizard.shared.ImportProgress.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Importer<T> implements Runnable {

	protected Logger logger = LoggerFactory.getLogger(Importer.class);

	protected CodelistRepository repository;
	protected ImportProgress progress;
	protected ImporterMapper<T> mapper;
	protected ImporterSource<T> source;
	protected AttributesMappings mappings;

	public Importer(CodelistRepository repository,
			ImporterSource<T> source,
			ImporterMapper<T> mapper,
			AttributesMappings mappings) {

		this.repository = repository;
		this.mapper = mapper;
		this.source = source;
		this.mappings = mappings;

		this.progress = new ImportProgress();
	}

	/**
	 * @return the progress
	 */
	public ImportProgress getProgress() {
		return progress;
	}

	@Override
	public void run() {
		logger.trace("starting import");
		try {
			progress.setStatus(Status.ONGOING);

			logger.trace("retrieving code list");
			T codelist = source.getCodelist();

			logger.trace("mapping codelist");
			Outcome outcome = mapper.map(mappings, codelist);

			Report report = outcome.report();
			logger.trace("is failed? {}", report.isFailure());
			logger.trace("Report: {}", report.toString());

			if (!report.isFailure()) {

				logger.trace("adding codelist");
				repository.add(outcome.result());
				progress.setStatus(Status.DONE);
			} else {
				logger.error("Import failed");
				progress.setReport(report.toString());
				progress.setStatus(Status.FAILED);

			}
		} catch(Throwable throwable)
		{
			logger.error("Error during import", throwable);
			progress.setReport(throwable.getMessage());
			progress.setStatus(Status.FAILED);
		}
	}

}
