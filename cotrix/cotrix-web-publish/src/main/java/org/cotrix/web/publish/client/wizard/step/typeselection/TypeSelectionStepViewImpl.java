package org.cotrix.web.publish.client.wizard.step.typeselection;

import org.cotrix.web.common.client.widgets.AlertDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class TypeSelectionStepViewImpl extends Composite implements TypeSelectionStepView {

	@UiTemplate("TypeSelectionStep.ui.xml")
	interface TypeSelectionStepUiBinder extends UiBinder<Widget, TypeSelectionStepViewImpl> {}
	private static TypeSelectionStepUiBinder uiBinder = GWT.create(TypeSelectionStepUiBinder.class);
	
	private Presenter presenter;
	
	@Inject
	AlertDialog alertDialog;
	
	public TypeSelectionStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("sdmxPanel")
	public void onSDMXClicked(ClickEvent event){
		presenter.onSDMXButtonClick();
	}
	
	@UiHandler("csvPanel")
	public void onCSVClicked(ClickEvent event){
		presenter.onCSVButtonClick();
	}
	
	@UiHandler("cometPanel")
	public void onCometClicked(ClickEvent event){
		presenter.onCometButtonClick();
	}
	
	public void alert(String message) {
		alertDialog.center(message);
	}
}
