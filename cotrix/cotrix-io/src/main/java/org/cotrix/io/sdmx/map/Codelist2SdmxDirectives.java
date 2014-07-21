package org.cotrix.io.sdmx.map;

import static java.lang.String.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.cotrix.io.sdmx.Constants.*;
import static org.cotrix.io.sdmx.SdmxElement.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Definition;
import org.cotrix.io.MapService.MapDirectives;
import org.cotrix.io.sdmx.SdmxElement;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class Codelist2SdmxDirectives implements MapDirectives<CodelistBean> {

	public static Codelist2SdmxDirectives DEFAULT = new Codelist2SdmxDirectives();


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

	
	private final Map<String,SdmxElement> codelistDirectives = new HashMap<>();
	private final Map<SdmxElement,List<Definition>> codelistDefs = new HashMap<>();
	
	private final Map<String,SdmxElement> codeDirectives = new HashMap<>();
	private final Map<SdmxElement,List<Definition>> codeDefs = new HashMap<>();
	
	private String agency = DEFAULT_SDMX_AGENCY;
	private String id;
	private String version;
	private Boolean isFinal;
	
	
	public Codelist2SdmxDirectives id(String id) {
		this.id = id;
		return this;
	}
	
	public Boolean isFinal() {
		return isFinal;
	}
	
	public Codelist2SdmxDirectives isFinal(Boolean isfinal) {
		this.isFinal = isfinal;
		return this;
	}
	
	public String id() {
		return id;
	}
	
	public String agency() {
		return agency;
	}
	
	public Codelist2SdmxDirectives agency(String agency) {
		this.agency = agency;
		return this;
	}
	
	public Codelist2SdmxDirectives version(String version) {
		this.version = version;
		return this;
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
	 * @param id the name
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
						
						if (codeDefs.get(element)==null)
							codeDefs.put(element,new ArrayList<Definition>());
						
						codeDefs.get(element).add(def);
						
						return Codelist2SdmxDirectives.this;
					}
					
					@Override
					public Codelist2SdmxDirectives forCodelist() {
						
						codelistDirectives.put(def.id(),element);
						
						if (codelistDefs.get(element)==null)
							codelistDefs.put(element,new ArrayList<Definition>());
						
						codelistDefs.get(element).add(def);
						
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
	
	///////////////
	
	private static String langError = "two or more attributes or links map onto %s with the same language";
	private static String noNameError = "no attribute or link maps onto a NAME for %s";
	
	
	public List<String> errors() {
		
		List<String> errors = new ArrayList<>();	
		
		List<Definition> names = codelistDefs.get(NAME);
		if (names==null || names.isEmpty())
			errors.add(format(noNameError,"the codelist"));
		
		names = codeDefs.get(NAME);
		if (names==null || names.isEmpty())
			errors.add(format(noNameError,"codes"));
		
		addSingleLanguageErrors(errors,NAME,codelistDefs.get(NAME));
		addSingleLanguageErrors(errors,NAME,codeDefs.get(NAME));
		addSingleLanguageErrors(errors,DESCRIPTION,codelistDefs.get(DESCRIPTION));
		addSingleLanguageErrors(errors,DESCRIPTION,codeDefs.get(DESCRIPTION));
		
		return errors;
	}
	
	
	private void addSingleLanguageErrors(List<String> errors, SdmxElement element,List<Definition> defs) {

		if (defs==null || defs.isEmpty())
			return;
		
		List<String> langs = new ArrayList<>();

		for (Definition def : defs) {
			
			String lang = languageOf(def);
			
			if (lang==null)
				lang="en";
			
			if (langs.contains(lang))
				errors.add(format(langError,element.toString()));
			else
				langs.add(lang);
		}
	}
	
}
