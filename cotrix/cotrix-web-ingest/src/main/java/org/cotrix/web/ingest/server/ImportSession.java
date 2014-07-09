/**
 * 
 */
package org.cotrix.web.ingest.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.cotrix.common.events.Current;
import org.cotrix.domain.user.User;
import org.cotrix.web.ingest.server.climport.ImportTaskSession;
import org.cotrix.web.ingest.shared.ImportProgress;
import org.cotrix.web.ingest.shared.UIAssetType;
import org.cotrix.web.ingest.shared.FileUploadProgress;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.MappingMode;
import org.virtualrepository.Asset;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SessionScoped
public class ImportSession implements Serializable {
	
	private static final long serialVersionUID = -1414667562737882248L;
	
	private FileUploadProgress uploadProgress;
	private UIAssetType codeListType;
	
	private File file;
	
	private Asset selectedAsset;
	
	private ImportMetadata guessedMetadata;

	private MappingMode guessedMappingMode;
	
	private ImportProgress importerProgress;
	
	private String importedCodelistName;
	
	private ImportTaskSession importTaskSession;
	
	@Inject @Current
	protected User user;
	
	public void clean() {
		uploadProgress = null;
		file = null;
		codeListType = null;
		
		selectedAsset = null;
		
		guessedMetadata = null;
		guessedMappingMode = null;
		importerProgress = null;
		importedCodelistName = null;
		
		importTaskSession = null;
	}
	
	public ImportTaskSession createImportTaskSession() throws IOException {
		importTaskSession = new ImportTaskSession(getInputStream(), selectedAsset, user.id());
		return importTaskSession;
	}
	
	@SuppressWarnings("resource")
	private InputStream getInputStream() throws FileNotFoundException {
		return file!=null?new FileInputStream(file):null;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
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
	 * @return the codeListType
	 */
	public UIAssetType getCodeListType() {
		return codeListType;
	}

	/**
	 * @param codeListType the codeListType to set
	 */
	public void setCodeListType(UIAssetType codeListType) {
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
	public ImportProgress getImporterProgress() {
		return importerProgress;
	}

	/**
	 * @param importer the importer to set
	 */
	public void setImporterProgress(ImportProgress importerProgress) {
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
