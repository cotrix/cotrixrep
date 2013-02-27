package org.cotrix.web.importwizard.client.presenter;

import java.util.ArrayList;

import com.google.inject.Inject;
import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.CotrixForm;
import org.cotrix.web.importwizard.client.view.form.UploadFormView;
import org.cotrix.web.importwizard.shared.CotrixImportModel;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

public class UploadFormPresenterImpl implements UploadFormPresenter {


	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private FileReader reader;
	private String filename = "";
	private UploadFormView view;
	private CotrixImportModel model;

	@Inject
	public UploadFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, UploadFormView view,CotrixImportModel model) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		initFileReader();
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

	private void initFileReader() {

		reader = new FileReader();
		reader.addLoadEndHandler(new LoadEndHandler() {
			public void onLoadEnd(LoadEndEvent event) {
				onLoadFileFinish();				
				ArrayList<String[]> data = parseCSV(reader.getStringResult());
				if(data.size() >= 2 ){
					model.getCsvFile().setDataAndHeader(data, data.get(0));
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


	public boolean isValidated() {
		if(model.getCsvFile().isEmpty())
			onError("Please browse csv file");
		return !model.getCsvFile().isEmpty();
	}

	public void onBrowseButtonClicked() {
		view.setFileUploadButtonClicked();
	}

	public void onDeleteButtonClicked() {
		model.getCsvFile().reset();
		view.setOnDeleteButtonClicked();
	}

	public void onLoadFileFinish() {
		view.setOnUploadFinish(filename);
	}

	public void onError(String message) {
		view.alert("Please select CSV file.");
	}

	public void onUploadFileChange(FileList fileList, String filename) {
		this.filename = filename;
		this.processFiles(fileList);
	}

}
