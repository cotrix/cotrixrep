package org.cotrix.application.validation;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static org.cotrix.application.validation.ValidationItem.*;
import static org.cotrix.common.Report.Item.Type.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.cotrix.application.ValidationService;
import org.cotrix.common.Report;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.LinkDefinition;

@Singleton
public class DefaultValidationService implements ValidationService {

	private static String  codeLinkError = "%s (%s): '%s' occurrences violate constraint %s";
	
	@Override
	public Report validate(Codelist list) {
	
		notNull("codelist",list);
		
		return validateCodes(list,list.codes());

	}
	
	
	@Override
	public Report validate(Codelist codelist, Collection<Code> codes) {
		
		notNull("codelist",codelist);
		notEmpty("codes", codes);
		
		return validateCodes(codelist, codes);
		
	}
	
	@Override
	public Report validate(Codelist codelist, Code ... codes) {
		
		notNull("codelist",codelist);
		notEmpty("codes", codes);
		
		return validateCodes(codelist, asList(codes));
		
	}
	
	
	//funnel for public methods
	private Report validateCodes(Codelist codelist,Iterable<? extends Code> codes) {
		
		Report report = new Report();
		
		validateLinks(codelist,codes,report);
		
		//add here future forms of validation
		
		return report;
	}
	
	
	
	private void validateLinks(Codelist codelist,Iterable<? extends Code> codes,Report report) {
	
		Codelist.Private list = reveal(codelist);

		Map<String,LinkDefinition> links = new HashMap<>();
		
		for (LinkDefinition link : list.links())
			links.put(link.id(),link);
		
		for (Code code : codes)
			validateLinks(reveal(code), links, report);
		
	}
	
	private void validateLinks(Code.Private code, Map<String,LinkDefinition> linktypes, Report report) {
		
		//count link type occurrences
		Map<String,Integer> counts = new HashMap<>();
		
		for (Codelink link : code.links()) {
			
			LinkDefinition type = linktypes.get(link.definition().id());
	
			if (type==null)
					continue;
			
			Integer count = counts.get(type);
			counts.put(type.id(), count==null? 1 : count+1);
		
		}
		
		for (LinkDefinition type : linktypes.values()) {
			int count = counts.containsKey(type.id())? counts.get(type.id()) : 0;  
			if (!type.range().inRange(count))
				report.log(
						item(code.id(),format(codeLinkError,code.qname(), code.id(),type.qname(),type.range())
				)).as(ERROR);
		}
	}

}
