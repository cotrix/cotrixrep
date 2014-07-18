package org.cotrix.domain.attributes;

import static org.cotrix.domain.attributes.Facet.*;
import static org.cotrix.domain.common.Ranges.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Range;
import org.cotrix.domain.trait.Named;

/**
 * Recurring, cross-codelist attribute definitions, typically system-managed.
 *
 */
public enum CommonDefinition implements Named {
	
	//for now, we can make all from a local name (ns=cotrix's, type=system)
	//can overload make() later if we need different type of definitions.
	
	CREATION_TIME 		(make("created",once),VISIBLE,PUSBLISHEABLE),
	
	UPDATE_TIME 		(make("updated"),VISIBLE,PUSBLISHEABLE),
	UPDATED_BY 			(make("updatedBy"),VISIBLE),
	
	//markers 
	
	DELETED				(make("deleted"),true),
	INVALID				(make("invalid"),true),
	ANOTHER_MARKER      (make("another_marker"),true),
	
	PREVIOUS_VERSION 		(make("previous_version"),VISIBLE),
	PREVIOUS_VERSION_ID 	(make("previous_version_id")),
	PREVIOUS_VERSION_NAME 	(make("previous_version_name"),VISIBLE),
	
	SUPERSIDES          (make("supersides"),VISIBLE);

	
	//lookup table to re-constitute enums from persisted local names.
	private static Map<String,CommonDefinition> defs = new HashMap<>();

	private static List<CommonDefinition> markers;
	
	//cannot build table with constructor. this is the next best thing and is as generic.
	static {  
		
		List<CommonDefinition> mks = new ArrayList<>();
		
		for (CommonDefinition def : values()) {
			defs.put(def.qname().getLocalPart(), def);
			
			if (def.isMarker())
				mks.add(def);
		}
		
		markers = Collections.unmodifiableList(mks);
		
	}
	
	
	
	private final Definition def;
	private final boolean marker;
	private final List<Facet> facets = new ArrayList<Facet>();
	
	
	private CommonDefinition(Definition def,Facet ... facets) {
		this(def,false,facets);
	}
	
	private CommonDefinition(Definition def, boolean marker, Facet ... facets) {
		
		this.def= def;
		this.marker=marker;
		
		for (Facet f : facets)
			this.facets.add(f);
	}
	
	
	//name is taken to return a plain string...
	public QName qname() {
		return def.qname();
	}
	
	public boolean is(Facet facet) {
		return facets.contains(facet);
	}
	
	//trust this is not going to be changed..if paranoia ensues, copy-construct..
	public Definition get() {
		return def;
	}
	
	public boolean isMarker() {
		return marker;
	}
	
	//just handy, cleans client code
	public Definition.State state() {
		return reveal(def).state();
	}
	
	//enables behavioural forks (e.g. store full def or just name...) 
	public static boolean isCommon(String name) {
		return commonDefinitionFor(name)!=null;
	}
	
		public static boolean isCommon(String name, Facet facet) {
			return commonDefinitionFor(name)!=null && commonDefinitionFor(name).is(facet);
		}
	
	public static boolean isCommon(QName name) {
		return isCommon(name.getLocalPart());
	}
	
	public static boolean isCommon(QName name,Facet facet) {
		return isCommon(name.getLocalPart(),facet);
	}
	
	//reconstitution from local names
	public static CommonDefinition commonDefinitionFor(String name) {
		return defs.get(name);
	}
	
	
	public static List<CommonDefinition> markers() {
		return markers;  ///it's immutable
	}
	
	//helper
	private static Definition make(String name) {
		return make(name,atmostonce);
	}
	
	//helper
	private static Definition make(String name,Range range) {
		return definition().name(q(NS,name)).is(SYSTEM_TYPE).occurs(range).build();
	}
	
	
	
	
}
