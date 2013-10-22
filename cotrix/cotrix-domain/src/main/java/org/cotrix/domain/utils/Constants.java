package org.cotrix.domain.utils;

import static org.cotrix.domain.dsl.Codes.*;

import javax.xml.namespace.QName;

public class Constants {

	public static final String NS = "http://cotrix.org";

	public static final QName DESCRIPTION_TYPE = q(NS,"description");
	public static final QName ANNOTATION_TYPE = q(NS,"annotation");
	public static final QName NAME_TYPE = q(NS,"name");
	
	public static final QName DEFAULT_TYPE = DESCRIPTION_TYPE;
	
	public static final QName NAME = q(NS,"name");
	
	
	
	public static final String NULL_STRING ="__ignore__";
	public static final QName NULL_QNAME = q("__ignore__");
}
