package org.cotrix.web.publish.client.wizard.step.formatselection;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class FormatSelectionStepViewImpl extends Composite implements FormatSelectionStepView {

	@UiTemplate("FormatSelectionStep.ui.xml")
	interface FormatSelectionStepUiBinder extends UiBinder<Widget, FormatSelectionStepViewImpl> {}
	private static FormatSelectionStepUiBinder uiBinder = GWT.create(FormatSelectionStepUiBinder.class);
	
	@UiField
	TableCellElement csvCell;
	
	@UiField
	TableCellElement sdmxCell;
	
	@UiField
	TableCellElement cometCell;
	
	private TableCellElement[] cells;
	
	private Presenter presenter;
	
	public FormatSelectionStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		cells = new TableCellElement[]{csvCell, sdmxCell, cometCell};
	}
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	private void updateCellsWidth() {
		List<TableCellElement> visibleCells = new ArrayList<TableCellElement>();
		for (TableCellElement cell:cells) if (isVisible(cell)) visibleCells.add(cell);
		
		int width = 100/visibleCells.size();
		for (TableCellElement cell:visibleCells) cell.getStyle().setWidth(width, Unit.PCT);
	}
	
	private boolean isVisible(TableCellElement cell) {
		return !Display.NONE.equals(cell.getStyle().getDisplay());
	}
	
	private void setVisible(TableCellElement cell, boolean visible) {
		String value = visible?"table-cell":"none";
		cell.getStyle().setProperty("display", value);
	}
	
	public void setCSVVisible(boolean visible) {
		setVisible(csvCell, visible);
		updateCellsWidth();
	}
	
	public void setSDMXVisible(boolean visible) {
		setVisible(sdmxCell, visible);
		updateCellsWidth();
	}
	
	public void setCometVisible(boolean visible) {
		setVisible(cometCell, visible);
		updateCellsWidth();
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

}
