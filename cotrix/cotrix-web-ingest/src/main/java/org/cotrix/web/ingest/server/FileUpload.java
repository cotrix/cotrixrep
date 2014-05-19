package org.cotrix.web.ingest.server;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.cotrix.web.common.server.util.FileNameUtil;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.ingest.server.upload.CodeListTypeGuesser;
import org.cotrix.web.ingest.server.upload.MappingsManager;
import org.cotrix.web.ingest.server.upload.PreviewDataManager;
import org.cotrix.web.ingest.server.upload.UploadProgressListener;
import org.cotrix.web.ingest.server.util.ParsingHelper;
import org.cotrix.web.ingest.server.util.ParsingHelper.InvalidSdmxException;
import org.cotrix.web.ingest.shared.UIAssetType;
import org.cotrix.web.ingest.shared.FileUploadProgress;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.FileUploadProgress.Status;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
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

	@Inject
	protected ParsingHelper parsingHelper;

	@Inject
	protected ImportSession session;

	@Inject
	protected PreviewDataManager previewDataManager;

	@Inject
	protected MappingsManager mappingsManager;

	public FileUpload()
	{
		// Create a factory for disk-based file items
		factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		File repository = new File(System.getProperty("java.io.tmpdir"));
		logger.trace("Set file repository to {}", repository.getAbsolutePath());
		factory.setRepository(repository);

		typeGuesser = new CodeListTypeGuesser();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {

		logger.trace("processing upload request");

		FileUploadProgress uploadProgress = new FileUploadProgress(0, Status.ONGOING, null);
		session.setUploadProgress(uploadProgress);

		try {

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				logger.error("Expected multipart request");
				uploadProgress.setFailed("Bad HTTP request: Expected multipart request");
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
				uploadProgress.setFailed(Exceptions.toError(fue));
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, fue.getMessage());
				return;
			}

			if (fileField == null) {
				logger.error("Missing field "+FILE_FIELD_NAME+" in upload request");
				uploadProgress.setFailed("Bad HTTP request: Missing field "+FILE_FIELD_NAME+" in upload request");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing field "+FILE_FIELD_NAME);
				return;
			}

			logger.trace("Received file {} with size {} and content type {}", fileField.getName(), fileField.getSize(), fileField.getContentType());
			UIAssetType codeListType = typeGuesser.guess(fileField.getName(), fileField.getContentType(), fileField.getInputStream());

			if (codeListType == null) {
				logger.error("failed to guess the codelist type");
				uploadProgress.setFailed("Failed to guess the codelist type");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing field "+FILE_FIELD_NAME);
				return;
			}

			session.setFileField(fileField);
			uploadProgress.setCodeListType(codeListType);
			session.setCodeListType(codeListType);
			response.setStatus(HttpServletResponse.SC_OK);

			switch (codeListType) {
				case CSV: {

					previewDataManager.setup(fileField.getName(), fileField);
					uploadProgress.setProgress(95);

					String filename = FileNameUtil.toHumanReadable(fileField.getName());
					ImportMetadata metadata = new ImportMetadata();
					metadata.setName(filename);
					metadata.setVersion("1");
					session.setGuessedMetadata(metadata);
				} break;
				case SDMX: {
					try {
						mappingsManager.setDefaultSdmxMappings();

						CodelistBean codelistBean = parsingHelper.parse(fileField.getInputStream());
						String codelistName = codelistBean.getName();
						ImportMetadata metadata = new ImportMetadata();
						metadata.setOriginalName(codelistName);
						metadata.setName(codelistName);
						metadata.setVersion(codelistBean.getVersion());
						session.setGuessedMetadata(metadata);
					} catch(InvalidSdmxException invalidSdmxException) {
						uploadProgress.setFailed(Exceptions.toError("Invalid SDMX file", invalidSdmxException));
						response.setStatus(HttpServletResponse.SC_OK);
						return;
					}
				} break;
			}

			uploadProgress.setProgress(100);
			uploadProgress.setDone();
			response.setStatus(HttpServletResponse.SC_OK);

		} catch(Throwable e)
		{
			logger.error("Error during file post", e);
			uploadProgress.setFailed(Exceptions.toError(e));
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed "+e.getMessage());
		}
	}


}