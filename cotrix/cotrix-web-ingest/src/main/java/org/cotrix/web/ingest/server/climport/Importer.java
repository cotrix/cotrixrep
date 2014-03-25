/**
 * 
 */
package org.cotrix.web.ingest.server.climport;

import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.action.events.CodelistActionEvents;
import org.cotrix.action.events.CodelistActionEvents.Import;
import org.cotrix.common.Outcome;
import org.cotrix.common.Report;
import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.tx.Transactional;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.web.common.server.util.Reports;
import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.common.shared.Progress.Status;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.ingest.server.climport.ImporterSource.SourceParameterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class Importer {

	protected Logger logger = LoggerFactory.getLogger(Importer.class);
	
	@Inject
	private Event<CodelistActionEvents.Import> events;

	@Transactional
	public <T> void importCodelist(Progress progress, SourceParameterProvider<T> sourceParameterProvider,
			ImporterSource source,
			ImporterMapper<T> mapper,
			ImporterTarget target,
			ImportTaskSession session,
			BeanSession beanSession) {
		
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
				progress.setMappingFailed(true);
				progress.setStatus(Status.DONE);
				return;
			}

			logger.trace("adding codelist");
			Codelist codelist = outcome.result();
			target.save(codelist, session.getMetadata().isSealed(), session.getOwnerId());

			events.fire(new Import(codelist.id(), codelist.name(), codelist.version(), beanSession));
			
			progress.setStatus(Status.DONE);

		} catch(Throwable throwable)
		{
			logger.error("Error during the import", throwable);
			progress.setFailed(new ServiceException(throwable.getMessage()));
		}
	}
}
