package org.cotrix.domain.attributes;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Named;

/**
 * Recurring, cross-codelist attribute definitions, typically system-managed.
 *
 */
public enum CommonDefinition implements Named {

	//for now, we can make all from a local name (ns=cotrix's, type=system)
	//can overload make() later if we need different type of definitions.
	
	CREATION_TIME 		(make("created")),
	
	UPDATE_TIME 		(make("updated")),
	UPDATED_BY 			(make("updatedBy")),
	
	STATUS				(make("status")),
	
	PREVIOUS_VERSION 	(make("previous_version")),
	PREVIOUS_VERSION_ID 	(make("previous_version_id")),
	PREVIOUS_VERSION_NAME 	(make("previous_version_name")),
	
	SUPERSIDES          (make("supersides"));
	
	
	//lookup table to re-constitute enums from persisted local names.
	private static Map<String,CommonDefinition> defs = new HashMap<String, CommonDefinition>();

	//cannot build table with constructor. this is the next best thing and is as generic.
	static {  
		
		for (CommonDefinition def : values())
			defs.put(def.qname().getLocalPart(), def);
	}
	
	
	
	private final Definition def;
	
	private CommonDefinition(Definition def) {
		this.def= def;
	}
	
	//name is taken to return a plain string...
	public QName qname() {
		return def.qname();
	}
	
	//trust this is not going to be changed..if paranoia ensues, copy-construct..
	public Definition get() {
		return def;
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
	
	
	//helper
	private static Definition make(String name) {
		return definition().name(q(NS,name)).is(SYSTEM_TYPE).build();
	}
}
