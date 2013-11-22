/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.tree;

import java.util.Arrays;
import java.util.List;

import org.cotrix.web.permissionmanager.client.codelists.tree.CodelistGroup.Version;
import org.cotrix.web.permissionmanager.client.resources.CodelistsResources;
import org.cotrix.web.permissionmanager.client.resources.PermissionsResources;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistTreeModel implements TreeViewModel {
	
	protected static List<CodelistGroup> GROUPS = Arrays.asList(
			new CodelistGroup("ASFIS", Arrays.asList(
					new Version(null, "1", "2011"),
					new Version(null, "2", "2012"),
					new Version(null, "3", "2013")
					)),
			new CodelistGroup("COUNTRIES", Arrays.asList(
					new Version(null, "11", "2009"),
					new Version(null, "21", "2011"),
					new Version(null, "31", "2013")
					))
			);
	
	public interface VersionItemTemplate extends SafeHtmlTemplates {
	    @Template("<div><img src=\"{0}\" style=\"vertical-align: middle;\"><span class=\"{1}\">version {2}</span></div>")
	    SafeHtml version(SafeUri image, String style, SafeHtml version);
	  }
	protected static final VersionItemTemplate VERSION_ITEM_TEMPLATE = GWT.create(VersionItemTemplate.class);
	
	protected static final AbstractCell<CodelistGroup> GROUP_CELL = new AbstractCell<CodelistGroup>() {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				CodelistGroup value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getName());
		}
	};
	
	protected static final AbstractCell<Version> VERSION_CELL = new AbstractCell<Version>() {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Version value, SafeHtmlBuilder sb) {
			SafeHtml html = VERSION_ITEM_TEMPLATE.version(PermissionsResources.INSTANCE.versionItem().getSafeUri(), CodelistsResources.INSTANCE.cellTreeStyle().versionItem(), SafeHtmlUtils.fromString(value.getVersion()));
			sb.append(html);
		}
	};
	
	protected ListDataProvider<CodelistGroup> dataProvider = new ListDataProvider<CodelistGroup>(GROUPS);
	protected SelectionModel<Version> selectionModel;

	/**
	 * @param dataProvider
	 */
	public CodelistTreeModel(SelectionModel<Version> selectionModel) {
		this.selectionModel = selectionModel;
	}

	@Override
	public <T> NodeInfo<?> getNodeInfo(final T value) {
		if (value == null) {
			return new DefaultNodeInfo<CodelistGroup>(dataProvider, GROUP_CELL);
		}
		
		if (value instanceof CodelistGroup) {
			CodelistGroup group = (CodelistGroup) value;
			
			final ListDataProvider<Version> versiondaDataProvider = new ListDataProvider<Version>(group.getVersions());
			/*dataProvider.addDataUpdatedHandler(new DataUpdatedHandler() {
				
				@Override
				public void onDataUpdated(DataUpdatedEvent event) {
					Log.trace("onDataUpdated "+value);
					versiondaDataProvider.refresh();
				}
			});*/
			return new DefaultNodeInfo<Version>(versiondaDataProvider, VERSION_CELL, selectionModel, null);
		}
		return null;
	}

	@Override
	public boolean isLeaf(Object value) {
		return value != null && !(value instanceof CodelistGroup);
	}

}
