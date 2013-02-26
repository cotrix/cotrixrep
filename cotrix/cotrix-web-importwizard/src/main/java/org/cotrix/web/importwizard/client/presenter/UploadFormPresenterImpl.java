package org.cotrix.web.importwizard.client.presenter;

import java.util.ArrayList;

import com.google.inject.Inject;
import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.CotrixForm;
import org.cotrix.web.importwizard.client.view.form.UploadFormView;
import org.vectomatic.file.ErrorCode;
import org.vectomatic.file.File;
import org.vectomatic.file.FileError;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.events.ErrorEvent;
import org.vectomatic.file.events.ErrorHandler;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;
import org.vectomatic.file.events.LoadStartEvent;
import org.vectomatic.file.events.LoadStartHandler;
import org.vectomatic.file.events.ProgressEvent;
import org.vectomatic.file.events.ProgressHandler;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class UploadFormPresenterImpl implements UploadFormPresenter {


	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private FileReader reader;
	private String filename = "";
	private UploadFormView view;

    @Inject
	public UploadFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, UploadFormView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		initFileReader(filename);
	}

	private ArrayList<String[]> parseCSV(String csv) {
		csv = csv.replaceAll("\"", "");
		String[] lines = csv.split("\n");
		ArrayList<String[]> results = new ArrayList<String[]>();
		for (String line : lines) {
			System.out.println(line);
			String[] cells = line.split("\t|,|\r");
			results.add(cells);
		}
		return results;
	}

	private void initFileReader(final String filename) {

		reader = new FileReader();
		reader.addLoadEndHandler(new LoadEndHandler() {
			public void onLoadEnd(LoadEndEvent event) {
				onLoadFileFinish(filename);				
				ArrayList<String[]> data = parseCSV(reader.getStringResult());
				if(data.size() >= 2 ){
					//					importWizardModel.getCsvFile().setDataAndHeader(data, data.get(0));
				}else {
					onError("File must have more than 2 rows");
				}
			}
		});

		reader.addLoadStartHandler(new LoadStartHandler() {
			public void onLoadStart(LoadStartEvent event) {
			}
		});
		reader.addProgressHandler(new ProgressHandler() {
			public void onProgress(ProgressEvent event) {
			}
		});
		reader.addErrorHandler(new ErrorHandler() {
			public void onError(ErrorEvent event) {
				
			}
		});
	}
	private void processFiles(FileList files) {
		if (files.getLength() == 0)
			return;

		File file = files.getItem(0);
		String type = file.getType();
		try {
			if ("image/svg+xml".equals(type)) {
				onError("Only CSV or Text file.");
			} else if (type.startsWith("image/png")) {
				onError("Only CSV or Text file.");
			} else if (type.startsWith("image/")) {
				onError("Only CSV or Text file.");
			} else if (type.startsWith("text/")) {
				reader.readAsText(file);
				filename = file.getName();
			}
		} catch (Throwable t) {
		}

	}
	private void handleError(File file) {
		FileError error = reader.getError();
		String errorDesc = "";
		if (error != null) {
			ErrorCode errorCode = error.getCode();
			if (errorCode != null) {
				errorDesc = ": " + errorCode.name();
			}
		}
		
		onError("File loading error for file: " + file.getName() + "\n"+ errorDesc);
	}
	
	private boolean hasFile(){
		return (filename.equals("")?false:true);
	}
	
	public boolean isValidate() {
		if(!hasFile())
			onError("Please browse csv file");
		return hasFile();
	}


	public void onBrowseButtonClicked() {

	}

	public void onDeleteButtonClicked() {
		
	}

	public void onLoadFileFinish(String filename) {
		 
	}

	public void onError(String message) {
		
	}

}
