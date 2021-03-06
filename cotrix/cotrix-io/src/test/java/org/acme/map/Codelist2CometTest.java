package org.acme.map;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.utils.DomainConstants.*;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.comet.map.Codelist2CometDirectives;
import org.cotrix.io.comet.serialise.Comet2XmlDirectives;
import org.cotrix.test.ApplicationTest;
import org.fao.fi.comet.mapping.model.MappingData;
import org.junit.Test;

//integration tests

public class Codelist2CometTest extends ApplicationTest {

	@Inject
	MapService mapper;
	
	@Inject
	SerialisationService serialiser;
	
	Codelist list = codelist().name("cotrix_testlist").
			with(
					code().name(q(NS,"code1")).build()
				   ,code().name("code2")
							.attributes(
								 attribute().name("attr1").value("value1").build()
							   , attribute().name("attr2").value("value2").in("fr").build()
							   , attribute().name("attr3").ofType(NAME_TYPE).build()
							   , attribute().name("attr4").value("value4").ofType(NAME_TYPE).in("es").build()
				).build())
				.version("1.0")
				.build();
	
	@Test 
	public void codelist2Comet2Xml() {
		
		Codelist versioned = reveal(list).bump("2.0");
		
		Code newcode = code().name("code3").attributes( 
								attribute().name(SUPERSIDES.qname()).value("badone").description("typo").build()
							,   attribute().name(SUPERSIDES.qname()).value("badtwo").build()
						)
						.build();
		
		Codelist modified = modify(versioned).with(newcode).build();
				
		reveal(versioned).update(reveal(modified));
		
		Outcome<MappingData> outcome = mapper.map(versioned, new Codelist2CometDirectives());
		
		serialise(outcome.result());	
		
		System.out.println(outcome.report());
		
	}
	
	
	
	void serialise(MappingData data) {
		serialiser.serialise(data,System.out,Comet2XmlDirectives.DEFAULT);
	}
}
