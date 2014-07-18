/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.common.client.async.AsyncUtils;
import org.cotrix.web.common.client.async.AsyncUtils.SuccessCallback;
import org.cotrix.web.common.client.factory.UIDefaults;
import org.cotrix.web.common.client.factory.UIFactories;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.ManageServiceAsync;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class MarkerTypeUtil {
	
	public static final String ACTIVE_MARKER_VALUE = "TRUE";
	
	@Inject
	private ManageServiceAsync service;
	
	private Map<String, MarkerType> definitionToMarker;
	private EnumMap<MarkerType, String> markerToDefinition;
	
	@Inject
	private UIDefaults defaults;
	
	@Inject
	private UIFactories factories;
	
	@Inject
	private void loadCache() {
		service.getMarkersAttributeTypes(AsyncUtils.manageError(new SuccessCallback<List<UIAttributeType>>() {

			@Override
			public void onSuccess(List<UIAttributeType> result) {
				fillCache(result);
			}
		}));
	}
	
	private void fillCache(List<UIAttributeType> definitions) {
		
		if (definitions.size()!=MarkerType.values().length) throw new IllegalStateException("The marker types and the definitions have not the same cardinality defs: "+definitions.size()+" markerTypes: "+MarkerType.values().length);
		
		definitionToMarker = new HashMap<String, MarkerType>();
		markerToDefinition = new EnumMap<MarkerType, String>(MarkerType.class);
		
		for (UIAttributeType definition:definitions) {
			MarkerType type = MarkerType.fromDefinitionName(definition.getName().getLocalPart());
			if (type==null) throw new IllegalStateException("No MarkerType found for definition "+definition);
			definitionToMarker.put(definition.getId(), type);
			markerToDefinition.put(type, definition.getId());
		}
		
		if (definitionToMarker.size() != markerToDefinition.size() || definitionToMarker.size()!=MarkerType.values().length) throw new IllegalStateException("Markers not in sync");
		
	}
	
	public MarkerType resolve(UIAttribute attribute) {
		if (attribute.getDefinitionId()==null) return null;
		return definitionToMarker.get(attribute.getDefinitionId());
	}
	
	public UIAttribute findAttribute(List<UIAttribute> attributes, MarkerType type) {
		String markerDefinitionId = markerToDefinition.get(type);
		for (UIAttribute attribute:attributes) {
			if (attribute.getDefinitionId()!=null && attribute.getDefinitionId().equals(markerDefinitionId)) return attribute;
		}
		return null;
	}
	
	public UIAttribute toAttribute(MarkerType type, String description) {
		UIAttribute attribute = factories.createAttribute();
		attribute.setName(new UIQName(defaults.defaultNameSpace(), type.getDefinitionName()));
		attribute.setDefinitionId(markerToDefinition.get(type));
		attribute.setType(defaults.systemType());
		attribute.setDescription(description==null?"":description);
		attribute.setValue(ACTIVE_MARKER_VALUE);
		
		return attribute;
	}
}
