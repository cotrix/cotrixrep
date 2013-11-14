package org.cotrix.web.publish.client.wizard.step.repositorydetails;

import org.cotrix.web.share.shared.codelist.RepositoryDetails;

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
