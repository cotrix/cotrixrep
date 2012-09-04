package com.google.gwt.sample.contacts.test.jre;


import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.IAnswer;
import org.fao.figis.cbt.shared.ContactDetails;
import org.fao.figis.cbt.web.client.ContactsServiceAsync;
import org.fao.figis.cbt.web.client.common.ContactsColumnDefinitionsFactory;
import org.fao.figis.cbt.web.client.presenter.ContactsPresenter;
import org.fao.figis.cbt.web.client.view.ContactsView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ExampleJRETest extends TestCase {

  private ContactsPresenter contactsPresenter;
  private ContactsServiceAsync mockRpcService;
  private HandlerManager mockEventBus;
  private ContactsView<ContactDetails> mockView;
  private List<ContactDetails> contactDetails;

  @SuppressWarnings("unchecked")
  protected void setUp() {
    mockRpcService = createStrictMock(ContactsServiceAsync.class);
    mockEventBus = new HandlerManager(null);
    mockView = createStrictMock(ContactsView.class);
    contactsPresenter = new ContactsPresenter(mockRpcService, mockEventBus, 
        mockView, 
        ContactsColumnDefinitionsFactory.getTestContactsColumnDefinitions());
  }
  
  @SuppressWarnings("unchecked")
  public void testDeleteButton() {
    contactDetails = new ArrayList<ContactDetails>();
    contactDetails.add(new ContactDetails("0", "c_contact"));
    contactsPresenter.setContactDetails(contactDetails);
    
    mockRpcService.deleteContacts(isA(ArrayList.class), 
        isA(AsyncCallback.class));
    
    expectLastCall().andAnswer(new IAnswer() {
      public Object answer() throws Throwable {
        final Object[] arguments = getCurrentArguments();
        AsyncCallback callback =
                (AsyncCallback) arguments[arguments.length - 1];
        callback.onSuccess(new ArrayList<ContactDetails>());
        return null;
      }
    });
    
    replay(mockRpcService);
    contactsPresenter.onDeleteButtonClicked();
    verify(mockRpcService);
    
    List<ContactDetails> updatedContactDetails = 
      contactsPresenter.getContactDetails();
    
    assertEquals(0, updatedContactDetails.size());
    
  }
}
