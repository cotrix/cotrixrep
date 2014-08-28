/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor.menu;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.async.AsyncUtils.SuccessCallback;
import org.cotrix.web.common.client.widgets.Loader;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.codelist.codes.editor.menu.ColumnPanel.ColumnPanelListener;
import org.cotrix.web.manage.shared.Group;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import static org.cotrix.web.common.client.async.AsyncUtils.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ColumnsMenu extends PopupPanel {
	
	private static ColumnsMenuUiBinder uiBinder = GWT.create(ColumnsMenuUiBinder.class);

	interface ColumnsMenuUiBinder extends UiBinder<Widget, ColumnsMenu> {}
	
	interface ColumnsMenuStyle extends CssResource {
		String popup();
	}
	
	public interface GroupsProvider {
		public void getGroups(AsyncCallback<List<Group>> callback);
		public List<Group> getActiveGroups();
	}
	
	@UiField ColumnsMenuStyle style;
	
	@UiField
	Label name;
	
	@UiField
	ScrollPanel columnsScroller;
	
	@UiField
	VerticalPanel columnsContainer;
	
	@UiField Loader loader;
	
	private GroupsProvider provider;
	private SuccessCallback<List<Group>> callback;
	
	public ColumnsMenu() {
		add(uiBinder.createAndBindUi(this));
		setStyleName(style.popup());
		setModal(true);
		setAutoHideEnabled(true);
		setLoaderVisible(false);
	}
	
	private void setLoaderVisible(boolean visible) {
		loader.setVisible(visible);
		columnsScroller.setVisible(!visible);
	}
	
	public void show(UICodelist codelist, GroupsProvider provider, SuccessCallback<List<Group>> callback, UIObject target) {
		this.name.setText(codelist.getName().getLocalPart());
		this.name.setTitle(codelist.getName().getLocalPart());
		
		this.provider = provider;
		this.callback = callback;
		
		refresh();
		
		showRelativeTo(target);
	}
	
	private void refresh() {
		setLoaderVisible(true);
		provider.getGroups(manageAndReportError(new AsyncCallback<List<Group>>() {

			@Override
			public void onFailure(Throwable caught) {
				setLoaderVisible(false);
				hide();
			}

			@Override
			public void onSuccess(List<Group> result) {
				setGroups(result, provider.getActiveGroups());
				setLoaderVisible(false);
			}
		}));
	}
	
	private void setGroups(List<Group> groups, List<Group> activeGroups) {
		columnsContainer.clear();
		
		int index = 0;
		for (final Group group:groups) {
			final ColumnPanel panel = new ColumnPanel();
			panel.setListener(new ColumnPanelListener() {
				
				@Override
				public void onUpClicked() {
					moveUp(panel);
				}
				
				@Override
				public void onDownClicked() {
					moveDown(panel);
				}

				@Override
				public void onCheckClicked(boolean down) {
					active(panel, down);
				}
			});
			
			panel.setGroup(group);
			panel.setActive(activeGroups.contains(group));
			panel.setButtons(index!=0, index!=groups.size()-1);
			columnsContainer.add(panel);
			index++;
		}
	}
	
	private void moveUp(ColumnPanel panel) {
		int index = columnsContainer.getWidgetIndex(panel);
		columnsContainer.remove(index);
		columnsContainer.insert(panel, index-1);
		updateButtons(panel, index-1);
		updateButtons((ColumnPanel) columnsContainer.getWidget(index), index);
	}
	
	private void moveDown(ColumnPanel panel) {
		int index = columnsContainer.getWidgetIndex(panel);
		columnsContainer.remove(index);
		columnsContainer.insert(panel, index+1);
		updateButtons(panel, index+1);
		updateButtons((ColumnPanel) columnsContainer.getWidget(index), index);
	}
	
	private void active(ColumnPanel panel, boolean active) {
		int index = columnsContainer.getWidgetIndex(panel);
		panel.setActive(active);
		updateButtons(panel, index);
	}
	
	private void updateButtons(ColumnPanel panel, int index) {
		panel.setButtons(index!=0, index != columnsContainer.getWidgetCount()-1);
	}

	private List<Group> getActiveGroups() {
		List<Group> actives = new ArrayList<Group>();
		for (int i = 0; i < columnsContainer.getWidgetCount(); i++) {
			Widget child = columnsContainer.getWidget(i);
			ColumnPanel panel = (ColumnPanel)child;
			if (panel.isActive()) actives.add(panel.getGroup());
		}
		return actives;
	}
	
	
	@UiHandler("refreshButton")
	void onRefreshClick(ClickEvent event) {
		refresh();
	}
	
	@UiHandler("doneButton")
	void onDoneClick(ClickEvent event) {
		Log.trace("Done");
		callback.onSuccess(getActiveGroups());
		hide();
	}

}
