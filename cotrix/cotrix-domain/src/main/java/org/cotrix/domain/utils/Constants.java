package org.cotrix.domain.utils;

import javax.xml.namespace.QName;

public class Constants {

	public static final String NS = "http://cotrix.org";

	public static final QName DESCRIPTION_TYPE = new QName(NS,"description");
	public static final QName ANNOTATION_TYPE = new QName(NS,"annotation");
	public static final QName NAME_TYPE = new QName(NS,"name");
	
	public static final QName DEFAULT_TYPE = DESCRIPTION_TYPE;
	
	public static final QName NAME = new QName(NS,"name");
}
