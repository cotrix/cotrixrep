/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.util.List;

import javax.inject.Inject;

import org.cotrix.common.BeanSession;
import org.cotrix.common.Outcome;
import org.cotrix.common.Report;
import org.cotrix.common.tx.Transactional;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.Reports;
import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Publisher {

	protected Logger logger = LoggerFactory.getLogger(Publisher.class);
	
	@Inject
	CodelistRepository repository;
	
	@Transactional
	public <T> void publish(PublishDirectives publishDirectives,
			PublishMapper<T> mapper,
			SerializationDirectivesProducer<T> serializationProducer,
			PublishToDestination destination, PublishStatus publishStatus, BeanSession session) {
		
		Progress progress = publishStatus.getProgress();
		
		try {
			logger.info("starting publishing");

			Codelist codelist = repository.lookup(publishDirectives.getCodelistId());
			
			logger.trace("mapping");
			Outcome<T> outcome = mapper.map(codelist,publishDirectives);

			Report report = outcome.report();

			logger.trace("is failed? {}", report.isFailure());

			logger.trace("found {} logs item", report.logs().size());

			List<ReportLog> logs = Reports.convertLogs(report.logs());
			publishStatus.setReportLogs(logs);
			publishStatus.setReport(report.toString());

			if (outcome.report().isFailure()) {
				logger.error("mapping failed");
				progress.setMappingFailed();
				return;
			}

			logger.trace("serializing");
			SerialisationDirectives<T> serialisationDirectives = serializationProducer.produce(publishDirectives);
			destination.publish(codelist, outcome.result(), serialisationDirectives, publishDirectives, publishStatus, session);

			logger.info("publish complete");
			progress.setDone();

		} catch(Throwable throwable)
		{
			logger.error("Error during codelist publishing", throwable);
			progress.setFailed(Exceptions.toError(throwable));
		}
	}
}
