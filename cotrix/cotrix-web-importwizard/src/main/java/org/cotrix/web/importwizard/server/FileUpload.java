package org.cotrix.web.importwizard.server;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.cotrix.web.importwizard.server.upload.CodeListTypeGuesser;
import org.cotrix.web.importwizard.server.upload.CsvParserConfigurationGuesser;
import org.cotrix.web.importwizard.server.upload.UploadProgressListener;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
import org.cotrix.web.importwizard.shared.FileUploadProgress.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FileUpload extends HttpServlet{

	private static final long serialVersionUID = 5551855314371513075L;

	private final String FILE_FIELD_NAME = "file";

	protected Logger logger = LoggerFactory.getLogger(FileUpload.class);
	protected DiskFileItemFactory factory;
	protected CodeListTypeGuesser typeGuesser;
	protected CsvParserConfigurationGuesser csvParserConfigurationGuesser;

	public FileUpload()
	{
		// Create a factory for disk-based file items
		factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		File repository = new File(System.getProperty("java.io.tmpdir"));
		logger.trace("Set file repository to {}", repository.getAbsolutePath());
		factory.setRepository(repository);

		typeGuesser = new CodeListTypeGuesser();
		csvParserConfigurationGuesser = new CsvParserConfigurationGuesser();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {

		logger.trace("processing upload request");

		HttpSession httpSession = request.getSession();
		WizardImportSession session = WizardImportSession.getImportSession(httpSession);
		FileUploadProgress uploadProgress = new FileUploadProgress(0, Status.ONGOING, null);
		session.setUploadProgress(uploadProgress);

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			logger.error("Expected multipart request");
			uploadProgress.setStatus(Status.FAILED);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
			return;
		}

		ServletFileUpload upload = new ServletFileUpload(factory);
		UploadProgressListener progressListener = new UploadProgressListener(uploadProgress);
		upload.setProgressListener(progressListener);

		FileItem fileField = null;
		try{
			// Parse the request
			List<FileItem> items = upload.parseRequest(request);

			for (FileItem item:items) {
				if (!item.isFormField() && FILE_FIELD_NAME.equals(item.getFieldName())) {
					fileField = item;
					break;
				}
			}

		} catch(FileUploadException fue)
		{
			logger.error("Error parsing upload request", fue);
			uploadProgress.setStatus(Status.FAILED);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, fue.getMessage());
			return;
		}

		if (fileField == null) {
			logger.error("Missing field "+FILE_FIELD_NAME+" in upload request");
			uploadProgress.setStatus(Status.FAILED);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing field "+FILE_FIELD_NAME);
			return;
		}

		logger.trace("Received file {} with size {} and content type {}", fileField.getName(), fileField.getSize(), fileField.getContentType());
		CodeListType codeListType = typeGuesser.guess(fileField.getName(), fileField.getContentType());

		if (codeListType == null) {
			logger.error("failed to guess the codelist type");
			uploadProgress.setStatus(Status.FAILED);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing field "+FILE_FIELD_NAME);
			return;
		}

		session.setFileField(fileField);
		uploadProgress.setCodeListType(codeListType);
		session.setCodeListType(codeListType);
		response.setStatus(HttpServletResponse.SC_OK);

		if (codeListType == CodeListType.CSV) {
			CsvParserConfiguration configuration = csvParserConfigurationGuesser.guessConfiguration(fileField);
			session.setCsvParserConfiguration(configuration);
			session.setCacheDirty(true);
		}

		uploadProgress.setStatus(Status.DONE);

	}


}