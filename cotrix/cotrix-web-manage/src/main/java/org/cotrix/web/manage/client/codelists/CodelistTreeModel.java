/**
 * 
 */
package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.group.Group;
import org.cotrix.web.common.client.widgets.group.GroupedDataProvider;
import org.cotrix.web.common.client.widgets.group.GroupedDataProvider.Grouper;
import org.cotrix.web.manage.client.resources.CodelistsResources;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.shared.UICodelistInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistTreeModel implements TreeViewModel {

	public enum Grouping implements Grouper<UICodelistInfo> {

		NONE(){

			@Override
			public String getGroupName(UICodelistInfo codelistInfo) {
				return null;
			}
		},

		BY_NAME() {

			@Override
			public String getGroupName(UICodelistInfo codelistInfo) {
				return ValueUtils.getLocalPart(codelistInfo.getName());
			}

		},
		BY_STATE() {

			@Override
			public String getGroupName(UICodelistInfo codelistInfo) {
				return codelistInfo.getState().toString().toUpperCase();
			}
		};

	}


	public interface CellTemplate extends SafeHtmlTemplates {
		@Template("<div><img src=\"{0}\" style=\"vertical-align: middle;\"><span class=\"{1}\">{2}</span></div>")
		SafeHtml version(SafeUri image, String style, SafeHtml version);

		@Template("<span title=\"{0}\">{0}</span>")
		SafeHtml groupName(String name);
	
		@Template("<div><span title=\"{0}\" style=\"color: #5674b9\">{0}</span><span class=\"{1}\">{2}</span></div>")
		SafeHtml codelist(String name, String versionStyle, String version);
	}

	protected static final CellTemplate CELL_TEMPLATES = GWT.create(CellTemplate.class);
	
	public static class ItemCell extends AbstractCell<UICodelistInfo> {

		private Grouping grouping = Grouping.BY_NAME;
		
		public void setGrouping(Grouping grouping) {
			this.grouping = grouping;
		}

		@Override
		public void render(Context context,	UICodelistInfo value, SafeHtmlBuilder sb) {
			switch (grouping) {
				case BY_NAME: {
					SafeUri imageUri = getImageSafeUri(value);
					SafeHtml html = CELL_TEMPLATES.version(imageUri, CodelistsResources.INSTANCE.cellTreeStyle().versionItem(), SafeHtmlUtils.fromString(value.getVersion()));
					sb.append(html);
				} break;
				case BY_STATE: {
					sb.append(CELL_TEMPLATES.codelist(ValueUtils.getLocalPart(value.getName()), CodelistsResources.INSTANCE.cellTreeStyle().versionItem(), value.getVersion()));
				} break;
				default:
					break;
			}
		}
		
		private SafeUri getImageSafeUri(UICodelistInfo codelist) {
			switch (codelist.getState()) {
				case draft: return CotrixManagerResources.INSTANCE.pencilBullet().getSafeUri();
				case locked: return CotrixManagerResources.INSTANCE.lockBullet().getSafeUri();
				case sealed: return CotrixManagerResources.INSTANCE.stopBullet().getSafeUri();
				default: return CotrixManagerResources.INSTANCE.versionItem().getSafeUri();
			}
		}
	};
	
	protected static final ItemCell ITEM_CELL = new ItemCell() ;

	protected static final AbstractCell<Group<UICodelistInfo>> GROUP_CELL = new AbstractCell<Group<UICodelistInfo>>() {

		@Override
		public void render(Context context,	Group<UICodelistInfo> value, SafeHtmlBuilder sb) {
			sb.append(CELL_TEMPLATES.groupName(value.getName()));
		}
	};

	protected CodelistsDataProvider dataProvider;
	private GroupedDataProvider<UICodelistInfo> groupedProvider;
	protected SelectionModel<UICodelistInfo> selectionModel;

	public CodelistTreeModel(CodelistsDataProvider dataProvider, SelectionModel<UICodelistInfo> selectionModel) {
		this.dataProvider = dataProvider;
		this.selectionModel = selectionModel;
		groupedProvider = new GroupedDataProvider<UICodelistInfo>(dataProvider, Grouping.BY_NAME);
	}

	@Override
	public <T> NodeInfo<?> getNodeInfo(final T value) {
		Log.trace("getNodeInfo value: "+value);
		if (value == null) {
			Log.trace("ROOT");
			return new DefaultNodeInfo<Group<UICodelistInfo>>(groupedProvider, GROUP_CELL);
		}

		if (value instanceof Group) {
			@SuppressWarnings("unchecked")
			Group<UICodelistInfo> group = (Group<UICodelistInfo>) value;
			return new DefaultNodeInfo<UICodelistInfo>(group.getItems(), ITEM_CELL, selectionModel, null);
			
		}
		return null;
	}	

	public void setGrouping(Grouping grouping) {
		ITEM_CELL.setGrouping(grouping);
		groupedProvider.setGrouper(grouping);
	}

	@Override
	public boolean isLeaf(Object value) {
		return value != null && !(value instanceof Group);
	}

}
