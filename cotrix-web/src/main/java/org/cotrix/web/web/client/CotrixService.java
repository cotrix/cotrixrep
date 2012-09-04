package org.cotrix.web.web.client;

import java.util.ArrayList;

import org.cotrix.web.shared.Contact;
import org.cotrix.web.shared.ContactDetails;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("contactsService")
public interface CotrixService extends RemoteService {

	Contact addContact(Contact contact);

	Boolean deleteContact(String id);

	ArrayList<ContactDetails> deleteCotrix(ArrayList<String> ids);

	ArrayList<ContactDetails> getContactDetails();

	Contact getContact(String id);

	Contact updateContact(Contact contact);
}
