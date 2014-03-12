package org.cotrix.web.publish.client.wizard.step.repositoryselection;

import org.cotrix.web.publish.shared.UIRepository;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(RepositorySelectionStepViewImpl.class)
public interface RepositorySelectionStepView {
	
	public interface Presenter {
		public void repositorySelected(UIRepository repository);
		public void repositoryDetails(UIRepository repository);
	}

	public void reset();
	void alert(String message);
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
