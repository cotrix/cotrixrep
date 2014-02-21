/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.common.Report.Log;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.common.shared.ReportLog.LogType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Reports {
	
	public static List<ReportLog> convertLogs(Throwable throwable)
	{
		List<ReportLog> reportLogs = new ArrayList<ReportLog>();
		ReportLog reportLog = new ReportLog(LogType.ERROR, throwable.getMessage());
		reportLogs.add(reportLog);
		return reportLogs;
	}
	
	public static List<ReportLog> convertLogs(List<Log> logs)
	{
		List<ReportLog> reportLogs = new ArrayList<ReportLog>();
		for (Log log:logs) {
			LogType type = convert(log.type());
			reportLogs.add(new ReportLog(type, log.message()));
		}

		return reportLogs;
	}

	protected static LogType convert(Log.Type type)
	{
		switch (type) {
			case INFO: return LogType.INFO;
			case ERROR: return LogType.ERROR;
			case WARN: return LogType.WARNING;
			default: throw new IllegalArgumentException("Unconvertible log type "+type);
		}
	}

}
