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
import org.cotrix.common.BeanSession;
import org.cotrix.common.Outcome;
import org.cotrix.common.Report;
import org.cotrix.common.async.TaskContext;
import org.cotrix.common.async.TaskUpdate;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.web.common.server.util.Reports;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.ingest.server.climport.ImporterSource.SourceParameterProvider;
import org.cotrix.web.ingest.shared.ImportResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class Importer {

	protected Logger logger = LoggerFactory.getLogger(Importer.class);
	
	@Inject
	private Event<CodelistActionEvents.Import> events;
	
	@Inject
	private TaskContext context;
	
	@Inject
	private BeanSession beanSession;

	public <T> ImportResult importCodelist(SourceParameterProvider<T> sourceParameterProvider,
			ImporterSource source,
			ImporterMapper<T> mapper,
			ImporterTarget target,
			ImportTaskSession session) throws Exception {
		
		logger.trace("starting import");
		//try {

			logger.trace("retrieving code list");
			context.save(new TaskUpdate(0, "retrieving codelist"));
			T data = source.getCodelist(session, sourceParameterProvider);

			logger.trace("mapping codelist");
			context.save(new TaskUpdate(0, "mapping codelist"));
			Outcome<Codelist> outcome = mapper.map(session, data);

			Report report = outcome.report();
			logger.trace("is failed? {}", report.isFailure());

			logger.trace("found {} logs item", report.logs().size());
			List<ReportLog> logs = Reports.convertLogs(report.logs());
			session.setLogs(logs);
			session.setReport(report.toString());

			if (report.isFailure()) {
				logger.error("Import failed");
				return new ImportResult(true);
			}

			logger.trace("adding codelist");
			Codelist codelist = outcome.result();
			target.save(codelist, session.getMetadata().isSealed(), session.getOwnerId());

			events.fire(new Import(origin(session),codelist.id(), codelist.qname(), codelist.version(), beanSession));
			
			return new ImportResult(codelist.id());

		/*} catch(Throwable throwable)
		{
			logger.error("Error during the import", throwable);
			progress.setFailed(Exceptions.toError(throwable));
		}*/
	}
	
	//helper
	String origin(ImportTaskSession session) {
		Asset asset = session.getAsset();
		return asset==null?"user desktop":asset.service().name().toString();
	}
}
