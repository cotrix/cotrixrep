/**
 * 
 */
package org.cotrix.web.importwizard.server;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.ImportService;
import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.Property;
import org.cotrix.web.importwizard.shared.RepositoryDetails;
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

	@Override
	public AssetDetails getAssetDetails(String assetId) {
		List<Property> properties = new ArrayList<Property>();
		properties.add(new Property("Agency", "FAO", "Agency name"));
		RepositoryDetails repository = new RepositoryDetails("iMarine", "CSV, SDMX",  "SDMX", properties);
		
		properties = new ArrayList<Property>();
		properties.add(new Property("Owner", "FAO", "Owner name"));
		properties.add(new Property("Size", "12", "Code list size"));
		AssetDetails assetDetails = new AssetDetails("123", "Fish", "SDMX", properties, repository);
		return assetDetails;
	}

}
