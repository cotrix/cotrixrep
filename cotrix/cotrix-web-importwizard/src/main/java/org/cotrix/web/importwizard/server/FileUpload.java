package org.cotrix.web.importwizard.server;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.mortbay.util.ajax.JSON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class FileUpload extends HttpServlet{
	private final String FILE_FIELD = "file";
	private final String MODEL_FIELD = "cotrixmodel";

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		ServletFileUpload upload = new ServletFileUpload();
		try{

			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();

				String name = item.getFieldName();
				if(name.equals(FILE_FIELD)){
					System.out.println("name = "+name);
					InputStream stream = item.openStream();

					// Process the input stream
					FileOutputStream out = new FileOutputStream("/Users/ton/Desktop/oooo/xxxuploadfile.txt");
					int len;
					byte[] buffer = new byte[8192];
					while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, len);
					}
					int maxFileSize = 10*(1024*1024); //10 megs max 
					/*if (out.size() > maxFileSize) { 
                         throw new RuntimeException("File is > than " + maxFileSize);
                     }*/
					out.flush();
					out.close();
				}else if(name.equals(MODEL_FIELD)){
					InputStream stream = item.openStream();
					int len;
					String xx = "";
					System.out.println("----start----");
					byte[] buffer = new byte[8192];
					while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
						String cotrixModelJSON = new String(buffer);
						xx = xx + cotrixModelJSON;
					}
					xx = xx.trim();
					Gson gson = new Gson();
					Map<String, String>[] foo = gson.fromJson(xx, Map[].class);
					for (Map<String, String> map : foo) {
						System.out.println(map.get("NAME"));
					}
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}

	}
}