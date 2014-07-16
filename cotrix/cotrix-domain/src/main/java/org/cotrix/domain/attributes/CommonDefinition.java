package org.cotrix.domain.attributes;

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
	
	CREATION_TIME 		(make("created",once)),
	
	UPDATE_TIME 		(make("updated")),
	UPDATED_BY 			(make("updatedBy")),
	
	//markers 
	
	DELETED				(make("deleted"),true),
	INVALID				(make("invalid"),true),
	
	PREVIOUS_VERSION 		(make("previous_version")),
	PREVIOUS_VERSION_ID 	(make("previous_version_id")),
	PREVIOUS_VERSION_NAME 	(make("previous_version_name")),
	
	SUPERSIDES          (make("supersides"));

	
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
	
	private CommonDefinition(Definition def) {
		this(def,false);
	}
	
	private CommonDefinition(Definition def, boolean marker) {
		this.def= def;
		this.marker=marker;
	}
	
	
	//name is taken to return a plain string...
	public QName qname() {
		return def.qname();
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
