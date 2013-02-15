package org.cotrix.web.importwizard.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

import org.cotrix.web.importwizard.client.GreetingService;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public HashMap<String, String> greetServer(String input) throws IllegalArgumentException {
		HashMap<String, String> mapCode = new HashMap<String, String>();
		String json = getLanguageInJSON();
		Gson gson = new Gson();
		HashMap<String, StringMap<?>> map = gson.fromJson(json, HashMap.class);
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			StringMap<?> obj = map.get(key);
		}
		System.out.println(map.size()+"xx"+mapCode.size());
		return mapCode;
	}
	
//	public HashMap<String, String> greetServer(String input) throws IllegalArgumentException {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("1", "test1");
//		map.put("2", "test2");
//		System.out.println(map.size()+"xx");
//		return map;
//	}

	private String getLanguageInJSON() {
		String language = "";
		try {
			File fileDir = new File(getServletContext().getRealPath("/")
					+ "files/languageCode.txt");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileDir), "UTF8"));

			String str;

			while ((str = in.readLine()) != null) {
				// System.out.println(str);
				language += str;
			}

			in.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return language;
	}

}
