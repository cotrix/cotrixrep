package org.cotrix.domain.utils;

import static org.cotrix.domain.dsl.Entities.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Range;
import org.cotrix.domain.common.Ranges;
import org.cotrix.domain.values.DefaultType;
import org.cotrix.domain.values.ValueType;

public class Constants {

	public static final String NS = "http://cotrix.org";
	
	//predefined types
	
	public static final QName DESCRIPTION_TYPE = q(NS,"description");
	public static final QName ANNOTATION_TYPE = q(NS,"annotation");
	public static final QName NAME_TYPE = q(NS,"name");
	public static final QName OTHER_CODE_TYPE = q(NS,"other_code");
	public static final QName OTHER_TYPE = q(NS,"other");

	public static final QName SYSTEM_TYPE = q(NS,"system");

	public static final QName defaultType = DESCRIPTION_TYPE;
	public static final QName[] DEFAULT_TYPES = new QName[]{DESCRIPTION_TYPE, ANNOTATION_TYPE, NAME_TYPE, OTHER_CODE_TYPE, OTHER_TYPE};
	
	public static final String defaultValue = "TRUE";
	public static final ValueType defaultValueType = new DefaultType();
	public static final Range defaultRange = Ranges.arbitrarily;
	
	//special values
	
	public static final String UNDEFINED_LINK_VALUE = "_!!_undefined_!!_";
	public static final String NULL_STRING ="__ignore__";
	public static final QName NULL_QNAME = q("__ignore__");
	public static final String NO_MAIL = "no@cotrix.mail";

}
