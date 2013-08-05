/**
 * 
 */
package org.cotrix.web.importwizard.server;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.importwizard.server.climport.Importer;
import org.cotrix.web.importwizard.shared.AttributesMappings;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.virtualrepository.Asset;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class WizardImportSession {
	
	public static final String SESSION_ATTRIBUTE_NAME = WizardImportSession.class.getSimpleName();
	
	public static WizardImportSession getImportSession(HttpSession httpSession){
		WizardImportSession importSession = (WizardImportSession) httpSession.getAttribute(SESSION_ATTRIBUTE_NAME);
		if (importSession == null) {
			importSession = new WizardImportSession();
			httpSession.setAttribute(SESSION_ATTRIBUTE_NAME, importSession);
		}
		return importSession;
	}
	
	public static WizardImportSession getCleanImportSession(HttpSession httpSession){
		WizardImportSession importSession = (WizardImportSession) httpSession.getAttribute(SESSION_ATTRIBUTE_NAME);
		if (importSession != null) {
			//TODO close it? release resources?	
		}
		
		importSession = new WizardImportSession();
		httpSession.setAttribute(SESSION_ATTRIBUTE_NAME, importSession);
		return importSession;
	}
	
	protected FileUploadProgress uploadProgress;
	protected FileItem fileField;
	protected CodeListType codeListType;
	
	protected CsvParserConfiguration csvParserConfiguration;
	protected PreviewData previewCache;
	protected boolean isCacheDirty;
	
	protected Asset selectedAsset;
	
	protected ImportMetadata guessedMetadata;
	
	protected AttributesMappings mappings;
	
	protected Importer<?> importer;

	/**
	 * @return the uploadProgress
	 */
	public FileUploadProgress getUploadProgress() {
		return uploadProgress;
	}
	
	/**
	 * @param uploadProgress the uploadProgress to set
	 */
	public void setUploadProgress(FileUploadProgress uploadProgress) {
		this.uploadProgress = uploadProgress;
	}

	/**
	 * @return the fileField
	 */
	public FileItem getFileField() {
		return fileField;
	}

	/**
	 * @param fileField the fileField to set
	 */
	public void setFileField(FileItem fileField) {
		this.fileField = fileField;
	}

	/**
	 * @return the codeListType
	 */
	public CodeListType getCodeListType() {
		return codeListType;
	}

	/**
	 * @param codeListType the codeListType to set
	 */
	public void setCodeListType(CodeListType codeListType) {
		this.codeListType = codeListType;
	}

	/**
	 * @return the csvParserConfiguration
	 */
	public CsvParserConfiguration getCsvParserConfiguration() {
		return csvParserConfiguration;
	}

	/**
	 * @param csvParserConfiguration the csvParserConfiguration to set
	 */
	public void setCsvParserConfiguration(
			CsvParserConfiguration csvParserConfiguration) {
		this.csvParserConfiguration = csvParserConfiguration;
	}

	/**
	 * @return the previewCache
	 */
	public PreviewData getPreviewCache() {
		return previewCache;
	}

	/**
	 * @param previewCache the previewCache to set
	 */
	public void setPreviewCache(PreviewData previewCache) {
		this.previewCache = previewCache;
	}

	/**
	 * @return the isCacheDirty
	 */
	public boolean isCacheDirty() {
		return isCacheDirty;
	}

	/**
	 * @param isCacheDirty the isCacheDirty to set
	 */
	public void setCacheDirty(boolean isCacheDirty) {
		this.isCacheDirty = isCacheDirty;
	}

	/**
	 * @return the metadata
	 */
	public ImportMetadata getGuessedMetadata() {
		return guessedMetadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setGuessedMetadata(ImportMetadata metadata) {
		this.guessedMetadata = metadata;
	}

	/**
	 * @return the mappings
	 */
	public AttributesMappings getMappings() {
		return mappings;
	}

	/**
	 * @param mappings the mappings to set
	 */
	public void setMappings(AttributesMappings mappings) {
		this.mappings = mappings;
	}

	/**
	 * @return the importer
	 */
	public Importer<?> getImporter() {
		return importer;
	}

	/**
	 * @param importer the importer to set
	 */
	public void setImporter(Importer<?> importer) {
		this.importer = importer;
	}

	/**
	 * @return the selectedAsset
	 */
	public Asset getSelectedAsset() {
		return selectedAsset;
	}

	/**
	 * @param selectedAsset the selectedAsset to set
	 */
	public void setSelectedAsset(Asset selectedAsset) {
		this.selectedAsset = selectedAsset;
	}

}
