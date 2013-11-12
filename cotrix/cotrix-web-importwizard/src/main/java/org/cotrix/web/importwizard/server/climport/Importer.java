/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.common.Outcome;
import org.cotrix.common.Report;
import org.cotrix.common.Report.Log;
import org.cotrix.domain.Codelist;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.DefaultLifecycleStates;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.importwizard.server.WizardImportSession;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.cotrix.web.importwizard.shared.ReportLog;
import org.cotrix.web.importwizard.shared.ReportLog.LogType;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.Progress.Status;
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
			List<ReportLog> logs = convertLogs(report.logs());
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

			State startState = metadata.isSealed()?DefaultLifecycleStates.sealed:DefaultLifecycleStates.draft;
			lifecycleService.start(codelist.id(), startState);

			progress.setStatus(Status.DONE);


		} catch(Throwable throwable)
		{
			logger.error("Error during import", throwable);
			progress.setStatus(Status.FAILED);
			importSession.setLogs(Collections.singletonList(new ReportLog(LogType.ERROR, throwable.getMessage())));
			importSession.setReport(throwable.getMessage());
		}
	}

	protected List<ReportLog> convertLogs(List<Log> logs)
	{
		List<ReportLog> reportLogs = new ArrayList<ReportLog>();
		for (Log log:logs) {
			LogType type = convert(log.type());
			reportLogs.add(new ReportLog(type, log.message()));
		}

		return reportLogs;
	}

	protected LogType convert(Log.Type type)
	{
		switch (type) {
			case INFO: return LogType.INFO;
			case ERROR: return LogType.ERROR;
			case WARN: return LogType.WARNING;
			default: throw new IllegalArgumentException("Unconvertible log type "+type);
		}
	}

}
