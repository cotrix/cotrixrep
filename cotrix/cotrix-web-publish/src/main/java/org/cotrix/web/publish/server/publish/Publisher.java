/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.util.List;

import org.cotrix.common.Outcome;
import org.cotrix.common.Report;
import org.cotrix.common.tx.Transactional;
import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.cotrix.web.publish.shared.PublishDirectives;
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
public class Publisher {

	protected Logger logger = LoggerFactory.getLogger(Publisher.class);
	
	@Transactional
	public <T> void publish(PublishDirectives publishDirectives,
			PublishMapper<T> mapper,
			SerializationDirectivesProducer<T> serializationProducer,
			PublishToDestination destination, PublishStatus publishStatus) {
		try {
			
			Progress progress = publishStatus.getProgress();
			
			logger.info("starting publishing");
			progress.setStatus(Status.ONGOING);

			logger.trace("mapping");
			Outcome<T> outcome = mapper.map(publishDirectives);

			Report report = outcome.report();

			logger.trace("is failed? {}", report.isFailure());

			logger.trace("found {} logs item", report.logs().size());

			List<ReportLog> logs = Reports.convertLogs(report.logs());
			publishStatus.setReportLogs(logs);
			publishStatus.setReport(report.toString());

			if (outcome.report().isFailure()) {
				logger.error("mapping failed");
				progress.setStatus(Status.FAILED);
				return;
			}

			logger.trace("serializing");
			SerialisationDirectives<T> serialisationDirectives = serializationProducer.produce(publishDirectives);
			destination.publish(outcome.result(), serialisationDirectives, publishDirectives, publishStatus);

			logger.info("publish complete");
			progress.setStatus(Status.DONE);

		} catch(Throwable throwable)
		{
			logger.error("Error during codelist publishing", throwable);
			publishStatus.getProgress().setStatus(Status.FAILED);
			publishStatus.setReportLogs(Reports.convertLogs(throwable));
			publishStatus.setReport(throwable.getMessage());
		}

	}

}
