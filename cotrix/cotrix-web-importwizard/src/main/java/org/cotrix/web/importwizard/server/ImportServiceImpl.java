package org.cotrix.web.importwizard.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

import org.cotrix.web.importwizard.client.CotrixModuleImport;
import org.cotrix.web.importwizard.client.ImportService;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ImportServiceImpl extends RemoteServiceServlet implements ImportService {

	public boolean sendToServer(String model) throws IllegalArgumentException {
		return true;
	}
	

}
