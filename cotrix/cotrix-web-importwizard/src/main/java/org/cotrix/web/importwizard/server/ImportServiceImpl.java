/**
 * 
 */
package org.cotrix.web.importwizard.server;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.ImportService;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.view.client.Range;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class ImportServiceImpl extends RemoteServiceServlet implements ImportService {

	@Override
	public boolean sendToServer(CotrixImportModel model) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void testBackendConnection() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<AssetInfo> getAssets(Range range) {
		ArrayList<AssetInfo> assets = new ArrayList<AssetInfo>();
		assets.add(new AssetInfo("123", "Fish", "CSV", "iMarine"));
		assets.add(new AssetInfo("321", "Gears", "SDMX", "D4Science"));
		assets.add(new AssetInfo("333", "Species Year 2013", "CSV", "Fish repo"));
		assets.add(new AssetInfo("324", "Country", "SDMX", "U.N. official Repo"));
		return assets;
	}

}
