package org.cotrix.map;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.comet.map.Codelist2CometDirectives;
import org.cotrix.io.comet.serialise.Comet2XmlDirectives;
import org.fao.fi.comet.mapping.model.MappingData;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

//integration tests

@RunWith(JeeunitRunner.class)
public class Codelist2CometTest {

	@Inject
	MapService mapper;
	
	@Inject
	SerialisationService serialiser;
	
	Codelist list = codelist().name("cotrix-testlist").
			with(
					code().name("code1").build()
					,code().name("code2").attributes(
							attribute().name("attr1").value("value1").build()
						   , attribute().name("attr2").value("value2").in("fr").build()
						   ,attribute().name("attr3").value("value3").ofType(NAME_TYPE).build()
							,attribute().name("attr4").value("value4").ofType(NAME_TYPE).in("es").build()
				).build())
				.version("1.0")
				.build();
	
	@Test 
	public void codelist2Comet2Xml() {
		
		Codelist versioned = reveal(list).bump("2.0");
		
		Outcome<MappingData> outcome = mapper.map(versioned,Codelist2CometDirectives.DEFAULT);
		
		serialise(outcome.result());	
		
		System.out.println(outcome.report());
		
	}
	
	
	
	void serialise(MappingData data) {
		serialiser.serialise(data,System.out,Comet2XmlDirectives.DEFAULT);
	}
}
