package org.cotrix.io.sdmx;

import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.io.sdmx.Constants.*;

import javax.xml.namespace.QName;

public enum SdmxElement {

	FINAL("final"), 
	AGENCY("agency"), 
	VALID_FROM("validFrom"), 
	VALID_TO("validTo"), 
	NAME("name",NAME_TYPE), 
	DESCRIPTION("description",DESCRIPTION_TYPE), 
	ANNOTATION("annotation",ANNOTATION_TYPE), 
	URI("uri");
	
	
	private final QName defaultMapping;
	private final QName defaultType;
	
	private SdmxElement(String defaultMapping) {
		this(defaultMapping,new QName(sdmx_ns,defaultMapping));
	}
	
	private SdmxElement(String defaultMapping, QName type) {
		this.defaultMapping=new QName(sdmx_ns,defaultMapping);
		this.defaultType = type;
	}
	
	
	public QName defaultName() {
		return defaultMapping;
	}
	
	public QName defaultType() {
		return defaultType;
	}
}
