package org.cotrix.web.publish.client;

import java.util.ArrayList;

import org.cotrix.web.share.shared.Codelist;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("publish")
public interface PublishService extends RemoteService {
	 ArrayList<Codelist> getAllCodelists() throws IllegalArgumentException;
	 CotrixImportModel getCodeListModel(String codelistId) ;
}
