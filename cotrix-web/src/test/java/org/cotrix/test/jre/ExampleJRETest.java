package org.cotrix.test.jre;


import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.cotrix.web.shared.ContactDetails;
import org.cotrix.web.web.client.CotrixServiceAsync;
import org.cotrix.web.web.client.common.CotrixColumnDefinitionsFactory;
import org.cotrix.web.web.client.presenter.CotrixPresenter;
import org.cotrix.web.web.client.view.CotrixView;
import org.easymock.IAnswer;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ExampleJRETest extends TestCase {

  private CotrixPresenter contactsPresenter;
  private CotrixServiceAsync mockRpcService;
  private HandlerManager mockEventBus;
  private CotrixView<ContactDetails> mockView;
  private List<ContactDetails> contactDetails;

  @SuppressWarnings("unchecked")
  protected void setUp() {
    mockRpcService = createStrictMock(CotrixServiceAsync.class);
    mockEventBus = new HandlerManager(null);
    mockView = createStrictMock(CotrixView.class);
    contactsPresenter = new CotrixPresenter(mockRpcService, mockEventBus, 
        mockView, 
        CotrixColumnDefinitionsFactory.getTestCotrixColumnDefinitions());
  }
  
  @SuppressWarnings("unchecked")
  public void testDeleteButton() {
    contactDetails = new ArrayList<ContactDetails>();
    contactDetails.add(new ContactDetails("0", "c_contact"));
    contactsPresenter.setContactDetails(contactDetails);
    
    mockRpcService.deleteCotrix(isA(ArrayList.class), 
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
