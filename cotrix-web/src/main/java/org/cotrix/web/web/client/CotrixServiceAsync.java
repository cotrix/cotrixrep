package org.cotrix.web.web.client;

import java.util.ArrayList;

import org.cotrix.web.shared.Contact;
import org.cotrix.web.shared.ContactDetails;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CotrixServiceAsync {

	public void addContact(Contact contact, AsyncCallback<Contact> callback);

	public void deleteContact(String id, AsyncCallback<Boolean> callback);

	public void deleteCotrix(ArrayList<String> ids, AsyncCallback<ArrayList<ContactDetails>> callback);

	public void getContactDetails(AsyncCallback<ArrayList<ContactDetails>> callback);

	public void getContact(String id, AsyncCallback<Contact> callback);

	public void updateContact(Contact contact, AsyncCallback<Contact> callback);
}
