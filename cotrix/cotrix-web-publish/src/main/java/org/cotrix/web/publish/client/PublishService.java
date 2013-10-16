package org.cotrix.web.publish.client;

import java.util.ArrayList;

import org.cotrix.web.share.shared.UIChanel;
import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("publish")
public interface PublishService extends RemoteService {
	 ArrayList<UICodelist> getAllCodelists() throws IllegalArgumentException;
	 ArrayList<UIChanel> getAllChanels();
	 void publishCodelist(String codelistID,ArrayList<String> chanels);
}