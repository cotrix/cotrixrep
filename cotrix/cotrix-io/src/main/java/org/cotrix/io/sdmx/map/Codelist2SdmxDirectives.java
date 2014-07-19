package org.cotrix.io.sdmx.map;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.io.sdmx.Constants.*;

import java.util.HashMap;
import java.util.Map;

import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Definition;
import org.cotrix.io.MapService.MapDirectives;
import org.cotrix.io.sdmx.SdmxElement;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class Codelist2SdmxDirectives implements MapDirectives<CodelistBean> {

	//API support clauses
	
	public static interface GetClause {
		
		SdmxElement get(Defined<?> a);
		
	}
	
	public static interface TargetClause {
		
		Codelist2SdmxDirectives forCodelist();
		
		Codelist2SdmxDirectives forCodes();
	}

	public static interface MappingClause {
		
		TargetClause to(SdmxElement e);
	}
	
	
	
	
	
	public static Codelist2SdmxDirectives DEFAULT = new Codelist2SdmxDirectives();

	private final Map<String,SdmxElement> codelistDirectives = new HashMap<>();
	private final Map<String,SdmxElement> codeDirectives = new HashMap<>();
	
	private String agency = DEFAULT_SDMX_AGENCY;
	private String name;
	private String version;
	private Boolean isFinal;
	
	
	public void name(String name) {
		this.name = name;
	}
	
	public Boolean isFinal() {
		return isFinal;
	}
	
	public void isFinal(Boolean isfinal) {
		this.isFinal = isfinal;
	}
	
	public String name() {
		return name;
	}
	
	public String agency() {
		return agency;
	}
	
	public void agency(String agency) {
		this.agency = agency;
	}
	
	public void version(String version) {
		this.version = version;
	}
	
	public String version() {
		return version;
	}
	
	public GetClause forCodelist() {
		
		return new GetClause() {
			
			public SdmxElement get(Defined<?> m) {
				
				return codelistDirectives.get(m.definition().id());
				
			};
		};
		
	}
	
	public GetClause forCodes() {
		
		return new GetClause() {
			
			public SdmxElement get(Defined<?> m) {
				
				return codeDirectives.get(m.definition().id());				
			};
		};
		
	}
	
	/**
	 * Maps a definition name and type onto a SDMX element type
	 * @param name the name
	 * @param type the type
	 * @return the mapping clause
	 */
	public MappingClause map(final Definition def) {
		
		notNull("mapping subject",def);
		
		return new MappingClause() {
			
			@Override
			public TargetClause to(final SdmxElement element) {

				notNull("SDMX element",element);

				return new TargetClause() {
					
					@Override
					public Codelist2SdmxDirectives forCodes() {

						codeDirectives.put(def.id(),element);
						
						return Codelist2SdmxDirectives.this;
					}
					
					@Override
					public Codelist2SdmxDirectives forCodelist() {
						
						codelistDirectives.put(def.id(),element);
						
						return Codelist2SdmxDirectives.this;
					}
				};
				
				
			}
		};
		
		
	}
	
	
	public <T extends Definition> MappingClause map(Defined<T> m) {
		
		notNull("mapping subject",m);
		
		return map(m.definition());
		
	}
	
}
