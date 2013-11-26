/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.util.List;

import javax.enterprise.event.Event;

import org.cotrix.action.events.CodelistActionEvents;
import org.cotrix.common.Outcome;
import org.cotrix.common.Report;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.DefaultLifecycleStates;
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.web.importwizard.server.WizardImportSession;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.MappingMode;
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

	protected CodelistRepository repository;
	protected LifecycleService lifecycleService;
	protected Event<CodelistActionEvents.CodelistEvent> events;

	protected Progress progress;
	protected ImporterMapper<T> mapper;
	protected ImporterSource<T> source;
	protected WizardImportSession importSession;
	protected ImportMetadata metadata;
	protected List<AttributeMapping> mappings;
	protected MappingMode mappingMode;

	public Importer(
			ImporterSource<T> source,
			ImporterMapper<T> mapper,
			ImportMetadata metadata,
			List<AttributeMapping> mappings,
			MappingMode mappingMode,
			WizardImportSession importSession) {

		this.mapper = mapper;
		this.source = source;
		this.metadata = metadata;
		this.mappings = mappings;
		this.mappingMode = mappingMode;
		this.importSession = importSession;

		this.progress = new Progress();
	}
	
	/**
	 * @param lifecycleService the lifecycleService to set
	 */
	public void setLifecycleService(LifecycleService lifecycleService) {
		this.lifecycleService = lifecycleService;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(CodelistRepository repository) {
		this.repository = repository;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(Event<CodelistActionEvents.CodelistEvent> events) {
		this.events = events;
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
			T data = source.getCodelist();

			logger.trace("mapping codelist");
			Outcome<Codelist> outcome = mapper.map(metadata, mappings, mappingMode, data);

			Report report = outcome.report();
			logger.trace("is failed? {}", report.isFailure());
			//logger.trace("Report: {}", report.toString());

			logger.trace("found {} logs item", report.logs().size());
			List<ReportLog> logs = Reports.convertLogs(report.logs());
			importSession.setLogs(logs);
			importSession.setReport(report.toString());

			if (report.isFailure()) {
				logger.error("Import failed");
				progress.setStatus(Status.FAILED);
				return;
			}

			logger.trace("adding codelist");
			Codelist codelist = outcome.result();
			repository.add(codelist);
			
			events.fire(new CodelistActionEvents.Import(codelist.id(), codelist.name(), codelist.version()));

			State startState = metadata.isSealed()?DefaultLifecycleStates.sealed:DefaultLifecycleStates.draft;
			lifecycleService.start(codelist.id(), startState);

			progress.setStatus(Status.DONE);


		} catch(Throwable throwable)
		{
			logger.error("Error during import", throwable);
			progress.setStatus(Status.FAILED);
			importSession.setLogs(Reports.convertLogs(throwable));
			importSession.setReport(throwable.getMessage());
		}
	}

	

}
