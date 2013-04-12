package org.cotrix.web.publish.server;

import java.util.ArrayList;

import org.cotrix.web.publish.client.PublishService;
import org.cotrix.web.share.shared.Codelist;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PublishServiceImpl extends RemoteServiceServlet implements
		PublishService {

	public ArrayList<Codelist> getAllCodelists()throws IllegalArgumentException {
		Codelist c1 = new Codelist();
		c1.setName("Country");
		c1.setId(0);
		
		Codelist c2 = new Codelist();
		c2.setName("Continent");
		c2.setId(1);

		Codelist c3 = new Codelist();
		c3.setName("ASFIS");
		c3.setId(2);

		Codelist c4 = new Codelist();
		c4.setName("Language");
		c4.setId(3);

		Codelist c5 = new Codelist();
		c5.setName("FAO Major Water Area");
		c5.setId(4);

		Codelist c6 = new Codelist();
		c6.setName("Vessel Type");
		c6.setId(5);
		
		Codelist c7 = new Codelist();
		c7.setName("Gear Type");
		c7.setId(6);
		
		ArrayList<Codelist> list = new ArrayList<Codelist>();
		list.add(c1);
		list.add(c2);
		list.add(c3);
		list.add(c4);
		list.add(c5);
		list.add(c6);
		list.add(c7);
		
		return list;
	}
}
