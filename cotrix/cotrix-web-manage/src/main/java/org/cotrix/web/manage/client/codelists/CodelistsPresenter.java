package org.cotrix.web.manage.client.codelists;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;

import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.util.FilteredCachedDataProvider.Filter;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.LifecycleState;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.feature.ApplicationFeatures;
import org.cotrix.web.common.shared.feature.UIFeature;
import org.cotrix.web.manage.client.codelist.event.CreateNewVersionEvent;
import org.cotrix.web.manage.client.codelist.event.RemoveCodelistEvent;
import org.cotrix.web.manage.client.codelist.metadata.VersionDialog;
import org.cotrix.web.manage.client.codelist.metadata.VersionDialog.VersionDialogListener;
import org.cotrix.web.manage.client.codelists.CodelistTreeModel.Grouping;
import org.cotrix.web.manage.client.codelists.CodelistsFeatureCache.CodelistsFeatureCacheListener;
import org.cotrix.web.manage.client.codelists.CodelistsMenu.MenuButton;
import org.cotrix.web.manage.client.codelists.NewCodelistDialog.NewCodelistDialogListener;
import org.cotrix.web.manage.client.event.CreateNewCodelistEvent;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.event.OpenCodelistEvent;
import org.cotrix.web.manage.client.event.RefreshCodelistsEvent;
import org.cotrix.web.manage.shared.UICodelistInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsPresenter implements Presenter, CodelistsView.Presenter {
	
	interface CodelistsPresenterEventBinder extends EventBinder<CodelistsPresenter> {}

	@Inject @ManagerBus
	private EventBus managerBus;
	
	private CodelistsView view;
	
	@Inject
	private CodelistsDataProvider codelistDataProvider;
	
	@Inject
	private NewCodelistDialog newCodelistDialog;
	
	@Inject
	private VersionDialog versionDialog;
	
	private UICodelist lastSelected;
	
	@Inject
	private CodelistsFeatureCache featureCache;
	
	@Inject
	private CodelistsMenu codelistsMenu;
	
	private ByNameFilter nameFilter = new ByNameFilter();
	private ByStateFilter stateFilter = new ByStateFilter();
	private ByOwnershipFilter ownershipFilter = new ByOwnershipFilter();

	@Inject
	public CodelistsPresenter(CodelistsView view) {
		this.view = view;
		this.view.setPresenter(this);
	}
	
	@SuppressWarnings("unchecked")
	@Inject
	private void initFilters() {
		Log.trace("initFilters");
		codelistDataProvider.setFilters(new AndFilter<UICodelistInfo>(nameFilter, stateFilter, ownershipFilter));
		codelistDataProvider.setApplyFiltersOnLoad(true);
		codelistDataProvider.setComparator(SORT_BY_NAME_AND_VERSION);
	}
	
	@Inject
	private void bind(CodelistsPresenterEventBinder binder) {
		binder.bindEventHandlers(this, managerBus);
		codelistsMenu.setListener(new CodelistsMenu.Listener() {
			
			@Override
			public void onButtonClicked(MenuButton button) {
				menuButtonClicked(button);
			}
		});
	}
	
	@Inject
	private void featureBind(FeatureBinder featureBinder)
	{
		featureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setAddCodelistVisible(active);
			}
		}, ApplicationFeatures.CREATE_CODELIST);
		
		featureCache.addListener(new CodelistsFeatureCacheListener() {
			
			@Override
			public void onFeatureUpdate(String resourceId, Set<UIFeature> features) {
				if (lastSelected!=null && lastSelected.getId().equals(resourceId)) refreshButtonsState();
			}
		});
		
		refreshButtonsState();
	}
	
	@Inject
	protected void setupDialogs() {
		newCodelistDialog.setListener(new NewCodelistDialogListener() {
			
			@Override
			public void onCreate(String name, String version) {
				managerBus.fireEvent(new CreateNewCodelistEvent(name, version));
			}
		});
		versionDialog.setListener(new VersionDialogListener() {
			
			@Override
			public void onCreate(String id, String newVersion) {
				managerBus.fireEvent(new CreateNewVersionEvent(id, newVersion));
			}
		});
	}
	
	
	@EventHandler
	void onRefreshCodelists(RefreshCodelistsEvent event) {
		refreshCodeLists();
	}

	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		view.reloadData();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void onCodelistItemSelected(UICodelist codelist) {
		Log.trace("onCodelistItemSelected codelist: "+codelist);
		lastSelected = codelist;
		if (codelist!=null) managerBus.fireEvent(new OpenCodelistEvent(codelist));
		refreshButtonsState();
	}
	
	private void refreshButtonsState() {
		if (lastSelected!=null) {
			boolean canRemove = featureCache.hasFeature(lastSelected.getId(), ApplicationFeatures.REMOVE_CODELIST);
			view.setRemoveCodelistVisible(canRemove);
			boolean canVersion = featureCache.hasFeature(lastSelected.getId(), ApplicationFeatures.VERSION_CODELIST);
			view.setVersionCodelistVisible(canVersion);
		} else {
			view.setRemoveCodelistVisible(false);
			view.setVersionCodelistVisible(false);
		}
	}

	public void refreshCodeLists() {
		Log.trace("onRefreshCodeLists");
		view.reloadData();		
	}

	@Override
	public void onCodelistRemove(UICodelist codelist) {
		Log.trace("onCodelistRemove codelist: "+codelist);
		managerBus.fireEvent(new RemoveCodelistEvent(codelist));
	}

	@Override
	public void onCodelistCreate() {
		newCodelistDialog.showCentered();
	}

	
	@Override
	public void onCodelistNewVersion(UICodelist codelist) {
		versionDialog.setOldVersion(codelist.getId(), ValueUtils.getLocalPart(codelist.getName()), codelist.getVersion());
		versionDialog.showCentered();
	}

	@Override
	public void onShowMenu() {
		codelistsMenu.show(view.getMenuTarget());
	}

	@Override
	public void onFilterQueryChange(String query) {
		Log.trace("onFilterQueryChange query: "+query);
		nameFilter.setDisabled(query.isEmpty());
		nameFilter.setName(query);
		codelistDataProvider.applyFilters();
	}
	
	private void menuButtonClicked(MenuButton button) {
		Log.trace("menuButtonClicked button: "+button);
		switch (button) {
			case GROUP_BY_NONE: view.groupBy(Grouping.NONE); break;
			case GROUP_BY_NAME: view.groupBy(Grouping.BY_NAME); break;
			case GROUP_BY_STATE: view.groupBy(Grouping.BY_STATE); break;
			case FILTER_BY_STATE_DRAFT: stateFilter.switchState(LifecycleState.draft); break;
			case FILTER_BY_STATE_LOCKED: stateFilter.switchState(LifecycleState.locked); break;
			case FILTER_BY_STATE_SEALED: stateFilter.switchState(LifecycleState.sealed); break;
			case SHOW_ALL: ownershipFilter.setDisabled(true); break;
			case SHOW_USER: ownershipFilter.setDisabled(false); break;

			default:
				break;
		}
		codelistDataProvider.applyFilters();
	}
	
	protected class ByNameFilter implements Filter<UICodelistInfo> {

		private String name;
		private boolean disabled;

		public ByNameFilter() {
			name = "";
			disabled = true;
		}

		public void setName(String name) {
			this.name = name.toUpperCase();
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}

		@Override
		public boolean accept(UICodelistInfo data) {
			return disabled || ValueUtils.getLocalPart(data.getName()).toUpperCase().contains(name);
		}

	}
	
	protected class ByStateFilter implements Filter<UICodelistInfo> {

		private Set<LifecycleState> states;

		public ByStateFilter() {
			states = EnumSet.allOf(LifecycleState.class);
		}

		public void addState(LifecycleState state) {
			states.add(state);
		}
		
		public void removeState(LifecycleState state) {
			states.remove(state);
		}
		
		public void switchState(LifecycleState state) {
			if (states.contains(state)) states.remove(state);
			else states.add(state);
		}

		@Override
		public boolean accept(UICodelistInfo data) {
			return states.contains(data.getState());
		}

	}
	
	protected class ByOwnershipFilter implements Filter<UICodelistInfo> {

		private boolean disabled;

		public ByOwnershipFilter() {
			disabled = true;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}

		@Override
		public boolean accept(UICodelistInfo data) {
			return disabled || data.isOwner();
		}

	}
	
	protected class AndFilter<T> implements Filter<T> {

		private Filter<T>[] filters;

		@SuppressWarnings("unchecked")
		public AndFilter(Filter<T> ... filters) {
			this.filters = filters;
		}

		@Override
		public boolean accept(T data) {
			for (Filter<T> filter:filters) if (!filter.accept(data)) return false;
			return true;
		}

	}
	
	protected static final Comparator<UICodelistInfo> SORT_BY_NAME_AND_VERSION = new Comparator<UICodelistInfo>() {
		
		@Override
		public int compare(UICodelistInfo o1, UICodelistInfo o2) {
			int byName = String.CASE_INSENSITIVE_ORDER.compare(ValueUtils.getLocalPart(o1.getName()), ValueUtils.getLocalPart(o2.getName()));
			if (byName == 0) return String.CASE_INSENSITIVE_ORDER.compare(o1.getVersion(), o2.getVersion());
			return byName;
		}
	};
}
