package org.cotrix.web.ingest.client.step.preview;

import org.cotrix.web.ingest.client.util.PreviewDataGrid;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class PreviewStepViewImpl extends ResizeComposite implements PreviewStepView {
	
	protected static final int HEADER_ROW = 0;

	@UiTemplate("PreviewStep.ui.xml")
	interface PreviewStepUiBinder extends UiBinder<Widget, PreviewStepViewImpl> {}
	
	@Inject
	private PreviewStepUiBinder uiBinder;	
	
	@UiField (provided=true) 
	PreviewDataGrid preview;

	@Inject
	TablePreviewDataProvider dataProvider;
	
	@Inject
	private void init() {
		this.preview = new PreviewDataGrid(dataProvider, 25);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			preview.onResize();
			preview.resetScroll();
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setPresenter(Presenter presenter) {
	}
	
	public void updatePreview()
	{
		preview.loadData();
	}
}
