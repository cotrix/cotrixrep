package org.cotrix.web.publish.client.wizard.step.cometmapping;

import org.cotrix.web.publish.client.util.AttributeMappingPanel.DefinitionWidgetProvider;
import org.cotrix.web.publish.client.util.MappingPanel;
import org.cotrix.web.publish.client.util.MappingPanel.ReloadButtonHandler;
import org.cotrix.web.publish.shared.AttributesMappings;
import org.cotrix.web.publish.shared.Column;

import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CometMappingStepViewImpl extends ResizeComposite implements CometMappingStepView, ReloadButtonHandler {

	public static final DefinitionWidgetProvider<Column> PROVIDER = new DefinitionWidgetProvider<Column>() {

		@Override
		public Widget getWidget(Column mapping) {
			return null;
		}

		@Override
		public void include(Widget widget, boolean include) {
		}

		@Override
		public Column getMapping(Widget widget) {
			return null;
		}

	};

	private MappingPanel<Column> mappingPanel;

	private Presenter presenter;

	public CometMappingStepViewImpl() {
		mappingPanel = new MappingPanel<Column>(PROVIDER, "ATTRIBUTES", false);
		mappingPanel.setReloadHandler(this);

		initWidget(mappingPanel);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) mappingPanel.resetScroll();
	}

	@Override
	public void showMetadata(boolean visible) {
		mappingPanel.showMetadata(visible);
	}

	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setCsvName(String name) {
		mappingPanel.setName(name);
	}

	@Override
	public String getCsvName() {
		return mappingPanel.getName();
	}

	@Override
	public void setVersion(String version) {
		mappingPanel.setVersion(version);
	}

	@Override
	public String getVersion() {
		return mappingPanel.getVersion();
	}

	@Override
	public void setSealed(boolean sealed)
	{
		mappingPanel.setSealed(sealed);
	}

	@Override
	public boolean getSealed()
	{
		return mappingPanel.getSealed();
	}

	public void setMappingLoading()
	{
		mappingPanel.setMappingLoading();
	}

	public void unsetMappingLoading()
	{
		mappingPanel.unsetMappingLoading();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setMappings(AttributesMappings mapping)
	{
		mappingPanel.setMapping(mapping);
	}

	public void setCodeTypeError()
	{
		mappingPanel.setCodeTypeError();
	}

	public void cleanStyle()
	{
		mappingPanel.cleanStyle();
	}

	public AttributesMappings getMappings()
	{
		return mappingPanel.getMappings();
	}

	@Override
	public void onReloadButtonClicked() {
		presenter.onReload();
	}

}
