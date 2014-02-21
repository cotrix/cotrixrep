/**
 * 
 */
package org.cotrix.web.importwizard.server;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.apache.commons.fileupload.FileItem;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.User;
import org.cotrix.web.importwizard.server.climport.ImportTaskSession;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.cotrix.web.share.shared.Progress;
import org.virtualrepository.Asset;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SessionScoped
public class ImportSession implements Serializable {
	
	private static final long serialVersionUID = -1414667562737882248L;
	
	protected FileUploadProgress uploadProgress;
	protected FileItem fileField;
	protected CodeListType codeListType;
	
	protected Asset selectedAsset;
	
	protected ImportMetadata guessedMetadata;

	protected MappingMode guessedMappingMode;
	
	protected Progress importerProgress;
	
	protected String importedCodelistName;
	
	protected ImportTaskSession importTaskSession;
	
	@Inject @Current
	protected User user;
	
	public void clean() {
		uploadProgress = null;
		fileField = null;
		codeListType = null;
		
		selectedAsset = null;
		
		guessedMetadata = null;
		guessedMappingMode = null;
		importerProgress = null;
		importedCodelistName = null;
		
		importTaskSession = null;
	}
	
	public ImportTaskSession createImportTaskSession() throws IOException {
		importTaskSession = new ImportTaskSession((fileField!=null)?fileField.getInputStream():null, selectedAsset, user.id());
		return importTaskSession;
	}		

	/**
	 * @return the importTaskSession
	 */
	public ImportTaskSession getImportTaskSession() {
		return importTaskSession;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

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
	 * @return the mappingMode
	 */
	public MappingMode getGuessedMappingMode() {
		return guessedMappingMode;
	}

	/**
	 * @param mappingMode the mappingMode to set
	 */
	public void setGuessedMappingMode(MappingMode mappingMode) {
		this.guessedMappingMode = mappingMode;
	}

	/**
	 * @return the importer
	 */
	public Progress getImporterProgress() {
		return importerProgress;
	}

	/**
	 * @param importer the importer to set
	 */
	public void setImporterProgress(Progress importerProgress) {
		this.importerProgress = importerProgress;
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

	/**
	 * @return the importedCodelistName
	 */
	public String getImportedCodelistName() {
		return importedCodelistName;
	}

	/**
	 * @param importedCodelistName the importedCodelistName to set
	 */
	public void setImportedCodelistName(String importedCodelistName) {
		this.importedCodelistName = importedCodelistName;
	}

}
