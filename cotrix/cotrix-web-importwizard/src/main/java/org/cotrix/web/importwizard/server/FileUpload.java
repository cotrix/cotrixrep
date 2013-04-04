package org.cotrix.web.importwizard.server;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.cotrix.domain.Codelist;
import org.cotrix.importservice.ImportService;
import org.cotrix.importservice.Outcome;
import org.cotrix.importservice.tabular.csv.CSV2Codelist;
import org.cotrix.importservice.tabular.csv.CSVOptions;
import org.cotrix.importservice.tabular.mapping.AttributeMapping;
import org.cotrix.importservice.tabular.mapping.CodelistMapping;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.json.HeaderTypeJson;

import com.google.gson.Gson;

public class FileUpload extends HttpServlet{
	private final String FILE_FIELD = "file";
	private final String MODEL_FIELD = "cotrixmodel";

	@Inject
	ImportService service;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("import service is :"+service);
		resp.setContentType("text/html");
		resp.getWriter().printf("HELLO FILE UPLOAD SERVLET");
		if(service == null){
			resp.getWriter().printf("Service is null");
		}else{
			resp.getWriter().printf("Service is not null");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		ServletFileUpload upload = new ServletFileUpload();
		try{

			FileItemIterator iter = upload.getItemIterator(request);
			InputStream uploadFileStream = null;
			ArrayList<HeaderType> types = new ArrayList<HeaderType>();
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				if(name.equals(FILE_FIELD)){
					uploadFileStream = item.openStream();
					
				}else if(name.equals(MODEL_FIELD)){
					InputStream stream = item.openStream();
					int len;
					String json = "";
					byte[] buffer = new byte[8192];
					while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
						String cotrixModelJSON = new String(buffer);
						json = json + cotrixModelJSON;
					}
					json = json.trim();
					types = formJSON(json);
				}
			}
			
			if(uploadFileStream != null && types.size() > 0){
				Outcome<Codelist> outcome = save(types, uploadFileStream);
				System.out.println("out come -->>"+outcome.report());
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}

	}
	public static ArrayList<HeaderType> formJSON(String json) {
		ArrayList<HeaderType> types = new ArrayList<HeaderType>();
		Gson gson = new Gson();
		Map<String, String>[] foo = gson.fromJson(json, Map[].class);
		for (Map<String, String> map : foo) {
			HeaderType type = new HeaderType();
			String name = map.get(HeaderTypeJson.KEY_NAME);
			String value = map.get(HeaderTypeJson.KEY_VALUE);
			String relatedValue = map.get(HeaderTypeJson.KEY_RELATED_VALUE);
			if(name !=null && name.length() > 0){
				type.setName(name);
			}
			if(value !=null && value.length() > 0){
				type.setValue(value);
			}
			if(relatedValue !=null && relatedValue.length() > 0){
				type.setRelatedValue(relatedValue);
			}
			types.add(type);
		}

		return types;
	}
	private Outcome<Codelist> save(ArrayList<HeaderType> types,InputStream stream){
		CSVOptions options = new CSVOptions();
		options.setDelimiter('\t');

		CodelistMapping mapping = new CodelistMapping("3A_CODE");
		QName asfisName = new QName("asfis-2012");
		mapping.setName(asfisName);

		List<AttributeMapping> attrs = new ArrayList<AttributeMapping>();
		for (HeaderType type : types) {
			AttributeMapping attr = new AttributeMapping (type.getName());
			if(type.getRelatedValue()!=null){
				attr.setLanguage(type.getRelatedValue());
			}
			attrs.add(attr);

		}
		mapping.setAttributeMappings(attrs);

		CSV2Codelist directives = new CSV2Codelist(mapping, options);
		return service.importCodelist(stream, directives);
	}
}