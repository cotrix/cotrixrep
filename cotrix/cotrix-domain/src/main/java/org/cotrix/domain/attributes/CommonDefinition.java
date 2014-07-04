package org.cotrix.domain.attributes;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Named;

/**
 * Cross-codelist attribute definitions.
 *
 */
public enum CommonDefinition implements Named {

	CREATION_TIME 		(make("created")),
	
	UPDATE_TIME 		(make("updated")),
	UPDATED_BY 			(make("updatedBy")),
	
	STATUS				(make("status")),
	
	PREVIOUS_VERSION 	(make("previous_version")),
	PREVIOUS_VERSION_ID 	(make("previous_version_id")),
	PREVIOUS_VERSION_NAME 	(make("previous_version_name")),
	
	SUPERSIDES          (make("supersides"));
	
	
	//lookup table
	
	private static Map<QName,CommonDefinition> defs = new HashMap<QName, CommonDefinition>();

	//cannot be built in constructor. this is the next best thing.
	static {  
		for (CommonDefinition def : values())
			defs.put(def.qname(), def);
	}
	
	
	
	private final Definition def;
	
	private CommonDefinition(Definition def) {
		this.def= def;
	}
	
	public QName qname() {
		return def.qname();
	}
	
	public Definition get() {
		return def;
	}
	
	public Definition.State state() {
		return reveal(def).state();
	}
	
	public static boolean isCommon(QName name) {
		return defs.get(name)!=null;
	}
	
	public static CommonDefinition commonDefinitionFor(QName name) {
		return defs.get(name);
	}
	
	
	//helper
	private static Definition make(String name) {
		return definition().name(q(NS,name)).is(SYSTEM_TYPE).build();
	}
}
