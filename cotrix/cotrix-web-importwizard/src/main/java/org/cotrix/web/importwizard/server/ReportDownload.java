/**
 * 
 */
package org.cotrix.web.importwizard.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.cotrix.web.share.server.util.FileNameUtil;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ReportDownload extends HttpServlet {

	private static final long serialVersionUID = 664872546900003111L;
	
	@Inject
	protected ImportSession session;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String report = session.getImportTaskSession().getReport();
		if (report == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no report in session");
			return;
		}
		
		String filename = FileNameUtil.toValidFileName("report-"+session.getImportedCodelistName()+".log");
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="+filename);
		OutputStream os = response.getOutputStream();
		IOUtils.copy(new StringReader(report), os);
		os.flush();
		os.close();
	}

}
