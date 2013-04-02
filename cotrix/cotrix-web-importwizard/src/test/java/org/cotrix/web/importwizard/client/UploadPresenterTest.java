package org.cotrix.web.importwizard.client;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.anyObject;


//import javax.enterprise.inject.Model;

import org.cotrix.web.importwizard.client.presenter.UploadFormPresenter;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;
import org.cotrix.web.importwizard.client.view.form.UploadFormView;
import org.cotrix.web.importwizard.client.view.form.UploadFormViewImpl;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModelController;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
//import static org.mockito.Mockito.mock;
import com.google.gwt.event.shared.HandlerManager;
import com.google.inject.Inject;
//import static org.mockito.Mockito.when;

//@RunWith(JukitoRunner.class)
public class UploadPresenterTest { 
//extends GwtTestWithMockito{
	
	ImportServiceAsync rpcService;
	HandlerManager eventBus;
	UploadFormView<UploadFormViewImpl> view;
	CotrixImportModelController model;
	
	
	@Test
	public void foo() {
		rpcService = createStrictMock(ImportServiceAsync.class);
		eventBus = createStrictMock(HandlerManager.class);
		view = createStrictMock(UploadFormView.class);
		model = createStrictMock(CotrixImportModelController.class);
		
		expect(model.getCsvFile()).andReturn(new CSVFile());
		replay(model);
		
		view.setPresenter((UploadFormPresenter)anyObject());
		view.setFileUploadButtonClicked();
		view.setOnDeleteButtonClicked();
		
		replay(view);

		UploadFormPresenter presenter = new UploadFormPresenterImpl(rpcService, eventBus, view, model);
		
		presenter.onBrowseButtonClicked();
		presenter.onDeleteButtonClicked();
		verify(view);
		
		verify(model);
	}
}
