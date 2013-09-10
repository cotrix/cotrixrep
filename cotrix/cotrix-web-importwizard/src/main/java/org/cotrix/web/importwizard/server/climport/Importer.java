/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.io.map.Outcome;
import org.cotrix.io.map.Report;
import org.cotrix.io.map.Report.Log;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.importwizard.server.WizardImportSession;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.ImportProgress;
import org.cotrix.web.importwizard.shared.ImportProgress.Status;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.cotrix.web.importwizard.shared.ReportLog;
import org.cotrix.web.importwizard.shared.ReportLog.LogType;
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
	protected WizardImportSession importSession;
	protected List<AttributeMapping> mappings;
	protected MappingMode mappingMode;

	public Importer(CodelistRepository repository,
			ImporterSource<T> source,
			ImporterMapper<T> mapper,
			List<AttributeMapping> mappings,
			MappingMode mappingMode,
			WizardImportSession importSession) {

		this.repository = repository;
		this.mapper = mapper;
		this.source = source;
		this.mappings = mappings;
		this.mappingMode = mappingMode;
		this.importSession = importSession;

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
			Outcome outcome = mapper.map(mappings, mappingMode, codelist);

			Report report = outcome.report();
			logger.trace("is failed? {}", report.isFailure());
			//logger.trace("Report: {}", report.toString());
			
			logger.trace("found {} logs item", report.logs().size());
			List<ReportLog> logs = convertLogs(report.logs());
			importSession.setLogs(logs);
			importSession.setReport(report.toString());

			if (!report.isFailure()) {

				logger.trace("adding codelist");
				repository.add(outcome.result());
				progress.setStatus(Status.DONE);
			} else {
				logger.error("Import failed");
				progress.setStatus(Status.FAILED);
			}
			
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
