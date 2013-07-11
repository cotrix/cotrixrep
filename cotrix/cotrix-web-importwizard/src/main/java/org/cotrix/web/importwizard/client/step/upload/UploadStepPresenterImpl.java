package org.cotrix.web.importwizard.client.step.upload;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModelController;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.json.HeaderTypeJson;
import org.vectomatic.file.File;
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

import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UploadStepPresenterImpl implements UploadStepPresenter {

	private FileReader reader;
	private String filename = "";
	private UploadStepView view;
	private CotrixImportModelController model;

	@Inject
	public UploadStepPresenterImpl(UploadStepView view, CotrixImportModelController model) {
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public WizardStepConfiguration getConfiguration() {
		return new WizardStepConfiguration("Upload File", "Upload CSV File", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
	}
	
	public interface OnUploadFileFinish{
		public void uploadFileFinish(SubmitCompleteEvent event);
	}
	
	private OnUploadFileFinish onUploadFileFinish;
	
	public void go(HasWidgets container) {
		//container.clear();
		container.add(view.asWidget());
		initFileReader();
	}

	private ArrayList<String[]> parseCSV(String csv) {
		csv = csv.replaceAll("\"", "");
		String[] lines = csv.split("\n");
		ArrayList<String[]> results = new ArrayList<String[]>();
		for (String line : lines) {
//			System.out.println(line);
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
					String[] headers = data.remove(0);

					CSVFile  csvFile = new CSVFile();
					csvFile.setData(data);
					csvFile.setHeader(headers);
					model.setCsvFile(csvFile);
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
	public void reset(){
		view.reset();
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
	public boolean isComplete() {
		if(model.getCsvFile()== null || model.getCsvFile().getData() == null) {
			onError("Please browse a csv file");
			return false;
		}
		return (model.getCsvFile().getData() != null) ? true:false;
	}

	public void onBrowseButtonClicked() {
		view.setFileUploadButtonClicked();
	}

	public void onDeleteButtonClicked() {
		model.getCsvFile().setData(null);
		model.getCsvFile().setFilename(null);
		model.getCsvFile().setHeader(null);
		model.getCsvFile().setRowCount(0);
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
	
	public void submitForm() {
		ArrayList<HeaderType> types = this.model.getType();
		String json = HeaderTypeJson.toJSON(types).toString();
		view.setCotrixModelFieldValue(json);
		view.submitForm();
	}

	public void onSubmitComplete(SubmitCompleteEvent event) {
		onUploadFileFinish.uploadFileFinish(event);
	}
	
	public void setOnUploadFileFinish(OnUploadFileFinish onUploadFileFinish){
		this.onUploadFileFinish = onUploadFileFinish;
	}
}