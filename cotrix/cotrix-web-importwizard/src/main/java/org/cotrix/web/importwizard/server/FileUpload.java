package org.cotrix.web.importwizard.server;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.importwizard.server.upload.CodeListTypeGuesser;
import org.cotrix.web.importwizard.server.upload.CsvParserConfigurationGuesser;
import org.cotrix.web.importwizard.server.upload.FileNameUtil;
import org.cotrix.web.importwizard.server.upload.MappingGuesser;
import org.cotrix.web.importwizard.server.upload.UploadProgressListener;
import org.cotrix.web.importwizard.server.util.ParsingHelper;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
import org.cotrix.web.importwizard.shared.FileUploadProgress.Status;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.tabular.Table;

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

	@Inject
	protected ParsingHelper parsingHelper;

	@Inject
	protected MappingGuesser mappingsGuesser;

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
		WizardImportSession session = WizardImportSession.getCleanImportSession(httpSession);
		FileUploadProgress uploadProgress = new FileUploadProgress(0, Status.ONGOING, null);
		session.setUploadProgress(uploadProgress);

		try {

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

			switch (codeListType) {
				case CSV: {
					CsvParserConfiguration configuration = csvParserConfigurationGuesser.guessConfiguration(fileField);
					session.setCsvParserConfiguration(configuration);
					uploadProgress.setProgress(95);

					//TODO check if csv config is valid
					//FIXME duplicate code
					Table table = parsingHelper.parse(configuration, fileField.getInputStream());
					PreviewData previewData = parsingHelper.convert(table, !configuration.isHasHeader(), ParsingHelper.ROW_LIMIT);
					session.setPreviewCache(previewData);
					session.setCacheDirty(false);

					List<AttributeMapping> mappings = mappingsGuesser.guessMappings(table);
					session.setMappings(mappings);

					String filename = FileNameUtil.toHumanReadable(fileField.getName());
					ImportMetadata metadata = new ImportMetadata();
					metadata.setName(filename);
					session.setGuessedMetadata(metadata);
				} break;
				case SDMX: {
					List<AttributeMapping> mappings = mappingsGuesser.getSdmxDefaultMappings();
					session.setMappings(mappings);

					CodelistBean codelistBean = parsingHelper.parse(fileField.getInputStream());
					String codelistName = codelistBean.getName();
					ImportMetadata metadata = new ImportMetadata();
					metadata.setOriginalName(codelistName);
					metadata.setName(codelistName);
					session.setGuessedMetadata(metadata);
				} break;
			}

		} catch(Exception e)
		{
			logger.error("Error during file post", e);
			uploadProgress.setStatus(Status.FAILED);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed "+e.getMessage());
		}

		uploadProgress.setProgress(100);
		uploadProgress.setStatus(Status.DONE);

	}


}