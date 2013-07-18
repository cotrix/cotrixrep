package org.cotrix.web.importwizard.server;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.cotrix.web.share.shared.HeaderType;

public class FileUpload extends HttpServlet{

	private static final long serialVersionUID = 5551855314371513075L;
	
	private final String FILE_FIELD = "file";

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		ServletFileUpload upload = new ServletFileUpload();
		try{

			FileItemIterator iter = upload.getItemIterator(request);
			ArrayList<HeaderType> types = new ArrayList<HeaderType>();
			byte[] bytesUploadFile = null;
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				if(name.equals(FILE_FIELD)){
					bytesUploadFile = IOUtils.toByteArray(item.openStream());
				}
			}
			
			if(bytesUploadFile != null && types.size() > 0){
				/*InputStream bis = new ByteArrayInputStream(bytesUploadFile);
				Outcome<Codelist> outcome = save(types,bis);
				outcome.result();*/
				
				response.setContentType("text/html");
				//response.getWriter().printf(outcome.report());
				
//				getAllCodelists();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}