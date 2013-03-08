package org.cotrix.web.importwizard.client;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.cotrix.web.importwizard.client.presenter.FormWrapperPresenter;
import org.cotrix.web.importwizard.client.view.form.FormWrapperView;
import org.cotrix.web.importwizard.client.view.form.FormWrapperViewImpl;
import org.cotrix.web.importwizard.shared.CotrixImportModel;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTestWithMockito;
import com.googlecode.gwt.test.Mock;

@GwtModule("org.cotrix.web.importwizard.CotrixModuleImport")
public class FormWrapperPresenterTest extends GwtTestWithMockito {
	
	FormWrapperPresenter presenter;
	
	@Mock
	FormWrapperView<FormWrapperViewImpl> view;
	
	@Mock
	ImportServiceAsync rpcService;
	
	@Mock
	HandlerManager eventBus;
	
	@Mock
	HasWidgets container;

	@Mock
	CotrixImportModel model;
	
	String title = "title";
	int index = 1;
	
	@Before
	public void beforeEach() {
//		presenter = new FormWrapperPresenter(rpcService, eventBus, view, model, null, title, index);
//		presenter.go(container);
	}

	@Test
	public void testAddingToContainer() throws Exception {
		assertTrue(true);
		verify(view).asWidget();
		verify(container).clear();
		verify(container).add(null);
	}

}
