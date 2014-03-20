package org.cotrix.domain.utils;

import static org.cotrix.domain.dsl.Codes.*;

import javax.xml.namespace.QName;

public class Constants {

	public static final String NS = "http://cotrix.org";
	
	
	public static final QName DESCRIPTION_TYPE = q(NS,"description");
	public static final QName ANNOTATION_TYPE = q(NS,"annotation");
	public static final QName NAME_TYPE = q(NS,"name");
	public static final QName OTHER_CODE_TYPE = q(NS,"other_code");
	public static final QName OTHER_TYPE = q(NS,"other");
	public static final QName DEFAULT_TYPE = DESCRIPTION_TYPE;
	public static final QName SYSTEM_TYPE = q(NS,"system");
	
	public static final QName NAME = q(NS,"name");
	public static final QName CREATION_TIME = q(NS,"created");
	public static final QName UPDATE_TIME = q(NS,"updated");
	
	
	
	public static final String NULL_STRING ="__ignore__";
	public static final QName NULL_QNAME = q("__ignore__");
	
	public static final String NO_MAIL = "no@cotrix.mail";
}
