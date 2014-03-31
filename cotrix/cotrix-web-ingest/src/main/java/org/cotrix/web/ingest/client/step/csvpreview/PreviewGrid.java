package org.cotrix.web.ingest.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.common.shared.CsvConfiguration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PreviewGrid {

	public void loadData();

	public void resetScroll();

	public List<String> getHeaders();
	
	public CsvConfiguration getConfiguration();
	public void setConfiguration(CsvConfiguration configuration);

}