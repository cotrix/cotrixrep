package org.cotrix.web.ingest.client.step.repositorydetails;

import org.cotrix.web.common.shared.codelist.RepositoryDetails;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface RepositoryDetailsStepView {
	
	public interface Presenter {
	}
	
	public void setRepository(RepositoryDetails repository);
	Widget asWidget();
}
