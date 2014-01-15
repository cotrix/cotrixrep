package org.cotrix.neo.domain;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

public class Constants {
	
	public static final String id_prop="id";
	public static final String name_prop="name";
	public static final String fullname_prop="fullname";
	public static final String type_prop="type";
	public static final String value_prop="val";
	public static final String lang_prop="lang";
	public static final String email_prop="email";
	public static final String permissions_prop="permissions";
	public static final String roles_prop="roles";
	public static final String version_prop="ver";
	public static final String pwd_prop = "pwd";
	
	public static enum NodeType implements Label {
		
		ATTRIBUTE, CODE, CODELIST, IDENTITY, USER;
	}
	
	public static enum Relations implements RelationshipType {
		ATTRIBUTE, CODE
	}
}