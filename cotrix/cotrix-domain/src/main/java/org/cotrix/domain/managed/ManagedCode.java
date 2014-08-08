package org.cotrix.domain.managed;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.CommonDefinition;
import org.cotrix.domain.codelist.Code;


//read-only interface over codes with support for systemic attributes
public class ManagedCode extends ManagedEntity<Code> {

	//convenience over one-time wrapping
	public static ManagedCode manage(Code code) {
		return new ManagedCode(code);
	}
	
	
	public ManagedCode() {}
	
	
	public ManagedCode(Code code) {
		super(code);
	}
	
	public List<Attribute> markers() {
		
		List<Attribute> mks = new ArrayList<>();
		for (CommonDefinition def : CommonDefinition.markers()) {
			
			Attribute mk = attribute(def);
			
			if (mk!=null)
				mks.add(mk);
		}
		
		return mks;
			
	
	}
	
	
}
