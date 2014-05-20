/**
 * 
 */
package org.cotrix.web.publish.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.cotrix.web.common.server.util.FileNameUtil;
import org.cotrix.web.common.shared.Format;
import org.cotrix.web.publish.server.util.PublishSession;
import org.cotrix.web.publish.shared.DownloadType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 664872546900003111L;
	
	@Inject
	protected PublishSession session;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String dowloadTypeParameter = (String) request.getParameter(DownloadType.PARAMETER_NAME);
		if (dowloadTypeParameter == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter "+DownloadType.PARAMETER_NAME);
			return;
		}
		
		DownloadType downloadType = null;
		try {
			downloadType = DownloadType.valueOf(dowloadTypeParameter);
		} catch(IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown download type "+dowloadTypeParameter);
			return;
		}
		
		String formatParameter =  (String) request.getParameter(Format.PARAMETER_NAME);
		if (downloadType==DownloadType.RESULT && formatParameter == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter "+Format.PARAMETER_NAME);
			return;
		}
		
		Format format = null;
		if (formatParameter != null) {
			try {
				format = Format.valueOf(formatParameter);
			} catch(IllegalArgumentException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown format "+formatParameter);
				return;
			}
		}

		switch (downloadType) {
			case REPORT: flushReport(response); break;
			case RESULT: flushFile(response, format); break;
		}

	}
	
	protected void flushReport(HttpServletResponse response) throws IOException
	{
		String report = session.getPublishStatus().getReport();
		if (report == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no report in session");
			return;
		}
		
		String filename = FileNameUtil.toValidFileName("report-"+session.getPublishStatus().getPublishedCodelist().name().getLocalPart()+".log");
		Reader content = new StringReader(report);
		flushContent(response, filename, content);		
	}
	
	protected void flushFile(HttpServletResponse response, Format format) throws IOException
	{
		Object result = session.getPublishStatus().getPublishResult();
		if (result == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no result in session");
			return;
		}
		
		if (!(result instanceof File)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The result is not a file");
			return;
		}
		
		File file = (File) result;
		if (!file.exists()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The result is not more available");
			return;
		}
		
		String extension = "bin";
		switch (format) {
			case CSV: extension = "csv"; break;
			case SDMX: extension = "xml"; break;
			case COMET: extension = "xml"; break;
		}
		String filename = FileNameUtil.toValidFileName(session.getPublishStatus().getPublishedCodelist().name().getLocalPart()+"."+extension);
		Reader content = new FileReader(file);
		flushContent(response, filename, content);
		session.getPublishStatus().setPublishResult(null);
	}
	
	protected void flushContent(HttpServletResponse response, String fileName, Reader content) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);
		OutputStream os = response.getOutputStream();
		IOUtils.copy(content, os);
		os.flush();
		os.close();
		content.close();
	}

}
