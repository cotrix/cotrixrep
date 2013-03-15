package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenterImpl;

import com.google.inject.Inject;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTestWithMockito;

@GwtModule("org.cotrix.web.codelistmanager.CotrixModuleManager")
public class CodeListManagerPresenterTest extends GwtTestWithMockito {
	
//    @Inject
//    EchoPresenter.Display view;
/*
    EchoPresenter presenter;
    @Mock
    EchoView echoView;
    @Mock
    EchoServiceAsync rpcService;
    @Mock
    HandlerManager eventBus;
    @Mock
    HasWidgets container;

    @Before
    public void beforeEach() {
        presenter = new EchoPresenter(rpcService, eventBus, echoView);
        presenter.go(container);
    }

    @Test
    public void testAddingToContainer() throws Exception {
        assertTrue(true);
        verify(echoView).asWidget();
        verify(container).clear();
        verify(container).add(null);
    }

    @Test
    public void testEchoHello() throws Exception {
        when(echoView.getInputMessage()).thenReturn("hello");
        doSuccessCallback("hello")
            .when(rpcService).sendMessage(Mockito.eq("hello"), Mockito.any(AsyncCallback.class));
        presenter.onRunButtonClicked();
        Mockito.verify(rpcService).sendMessage(Mockito.eq("hello"), Mockito.any(AsyncCallback.class));
        Mockito.verify(echoView).getInputMessage();
        Mockito.verify(echoView).setEchoMessage(Mockito.eq("hello"));
    }

    @Test
    public void testEchoHiAndFail() throws Exception {
        when(echoView.getInputMessage()).thenReturn("Hi");
        doFailureCallback(new RuntimeException("expected mocked runtime exception"))
                .when(rpcService).sendMessage(Mockito.eq("Hi"), Mockito.any(AsyncCallback.class));
        presenter.onRunButtonClicked();
        Mockito.verify(rpcService).sendMessage(Mockito.eq("Hi"), Mockito.any(AsyncCallback.class));
        Mockito.verify(echoView).getInputMessage();
        Mockito.verify(echoView).setEchoMessage(Mockito.eq("FAIL !!!"));
    }*/
}
