/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.common.Log;
import org.cotrix.common.Report.Item;
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
		while(throwable != null) {
			ReportLog reportLog = new ReportLog(LogType.ERROR, throwable.getClass().getSimpleName()+": "+throwable.getMessage());
			reportLogs.add(reportLog);
			throwable = throwable.getCause();
		}
		return reportLogs;
	}
	
	public static String convertToString(Throwable throwable)
	{
		StringBuilder reportLogs = new StringBuilder();
		while(throwable != null) {
			reportLogs.append(throwable.getClass().getSimpleName()).append(": ").append(throwable.getMessage()).append("\n");
			throwable = throwable.getCause();
		}
		return reportLogs.toString();
	}

	public static List<ReportLog> convertLogs(List<Item> logs)
	{
		List<ReportLog> reportLogs = new ArrayList<ReportLog>();
		for (Item item:logs) {
			LogType type = convert(item.type());
			reportLogs.add(new ReportLog(type, item.message()));
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
