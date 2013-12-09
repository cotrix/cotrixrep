/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.util.List;

import org.cotrix.common.Outcome;
import org.cotrix.common.Report;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.web.importwizard.server.climport.ImporterSource.SourceParameterProvider;
import org.cotrix.web.share.server.util.Reports;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.Progress.Status;
import org.cotrix.web.share.shared.ReportLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Importer<T> implements Runnable {

	protected Logger logger = LoggerFactory.getLogger(Importer.class);

	protected Progress progress;
	protected ImporterMapper<T> mapper;
	protected SourceParameterProvider<T> sourceParameterProvider;
	protected ImporterSource source;
	protected ImporterTarget target;
	protected ImportTaskSession session;

	public Importer(
			SourceParameterProvider<T> sourceParameterProvider,
			ImporterSource source,
			ImporterMapper<T> mapper,
			ImporterTarget target,
			ImportTaskSession session) {

		this.sourceParameterProvider = sourceParameterProvider;
		this.mapper = mapper;
		this.source = source;
		this.target = target;
		this.session = session;

		this.progress = new Progress();
	}

	/**
	 * @return the progress
	 */
	public Progress getProgress() {
		return progress;
	}

	@Override
	public void run() {
		logger.trace("starting import");
		try {
			progress.setStatus(Status.ONGOING);

			logger.trace("retrieving code list");
			T data = source.getCodelist(session, sourceParameterProvider);

			logger.trace("mapping codelist");
			Outcome<Codelist> outcome = mapper.map(session, data);

			Report report = outcome.report();
			logger.trace("is failed? {}", report.isFailure());
			//logger.trace("Report: {}", report.toString());

			logger.trace("found {} logs item", report.logs().size());
			List<ReportLog> logs = Reports.convertLogs(report.logs());
			session.setLogs(logs);
			session.setReport(report.toString());

			if (report.isFailure()) {
				logger.error("Import failed");
				progress.setStatus(Status.FAILED);
				return;
			}

			logger.trace("adding codelist");
			Codelist codelist = outcome.result();
			target.save(codelist, session.getMetadata().isSealed(), session.getOwnerId());

			progress.setStatus(Status.DONE);

		} catch(Throwable throwable)
		{
			logger.error("Error during import", throwable);
			progress.setStatus(Status.FAILED);
			session.setLogs(Reports.convertLogs(throwable));
			session.setReport(throwable.getMessage());
		}
	}

	

}
