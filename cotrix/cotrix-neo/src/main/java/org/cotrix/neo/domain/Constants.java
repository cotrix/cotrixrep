package org.cotrix.neo.domain;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

public class Constants {
	
	public static final String id_prop="id";
	public static final String name_prop="name";
	public static final String fullname_prop="fullname";
	public static final String type_prop="type";
	public static final String shared_prop="shared";
	public static final String vtype_prop="vtype";
	public static final String value_prop="val";
	public static final String note_prop="note";
	public static final String lang_prop="lang";
	public static final String email_prop="email";
	public static final String permissions_prop="permissions";
	public static final String roles_prop="roles";
	public static final String version_prop="ver";
	public static final String pwd_prop = "pwd";
	public static final String state_prop = "state";
	public static final String target_prop = "target";
	public static final String function_prop = "function";
	public static final String range_prop = "range";
	public static final String cdef_prop = "cdef";
	public static final String entries_prop = "entries";
	
	public static enum NodeType implements Label {
		
		ATTRIBUTE, ATTRDEF, CODE, CODELIST, IDENTITY, LIFECYCLE, LOGBOOK, USER, LINKDEF, CODELINK;
	}
	
	public static enum Relations implements RelationshipType {
		ATTRIBUTE, DEFINITION, CODE, LINK, INSTANCEOF, LOL
	}
}
