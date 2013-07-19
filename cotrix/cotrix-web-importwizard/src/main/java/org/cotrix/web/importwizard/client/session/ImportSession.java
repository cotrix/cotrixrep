/**
 * 
 */
package org.cotrix.web.importwizard.client.session;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportSession {

	
	protected SourceType sourceType;
	
	public ImportSession()
	{
	}

	/**
	 * @return the sourceType
	 */
	public SourceType getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}
}
