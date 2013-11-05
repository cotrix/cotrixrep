package org.cotrix;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.io.sdmx.SdmxElement.*;

import java.util.Calendar;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.sdmx.SdmxElement;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.cotrix.io.sdmx.serialise.Sdmx2XmlDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.util.date.DateUtil;

import com.googlecode.jeeunit.JeeunitRunner;

//integration tests

@RunWith(JeeunitRunner.class)
public class Codelist2Sdmx {

	QName customNameType = new QName("customname");
	QName customDescriptionType = new QName("customdescription");
	QName customAnnotationType = new QName("customannotation");
	
	@Inject
	MapService mapper;
	
	@Inject
	SerialisationService serialiser;
	
	
	Codelist list = codelist("1").name("cotrix-testlist").
			with(
					code("1").name("code1").build()
					,code("2").name("code2").attributes(
							attr().name("attr1").value("value1").build()
						   , attr().name("attr2").value("value2").in("fr").build()
						   ,attr().name("attr3").value("value3").ofType(NAME_TYPE).build()
							,attr().name("attr4").value("value4").ofType(NAME_TYPE).in("sp").build()
				).build()).
				attributes(
							attr().name("list-attr1").value("value1").build()
							,attr().name("list-attr2").value("value2").ofType(customDescriptionType).in("sp").build()
						    ,attr().name("list-attr3").value("value3").ofType(customNameType).in("fr").build()
						   ,attr().name("list-attr4").value("value4").ofType(NAME_TYPE).build()
							,attr().name("list-attr5").value("value5").ofType(NAME_TYPE).in("sp").build()
							,attr().name("list-attr6").value("value6").ofType(ANNOTATION_TYPE).in("sp").build()
							,attr().name("list-attr7").value("value7").ofType(customAnnotationType).in("fr").build()
							,attr().name("list-attr8").value("value8").ofType("unmappedtype").build()
							,attr().name(VALID_FROM.defaultName()).value(DateUtil.formatDate(Calendar.getInstance().getTime())).build()
							,attr().name(VALID_TO.defaultName()).value("bad").build()
				)
				.version("1.0").build();
	
	@Test 
	public void codelist2Sdmx2Xml() {
		
		//a poor test, but cannot possibly work with smdx-source API!
		
		Codelist2SdmxDirectives directives = new Codelist2SdmxDirectives();
		
		directives.agency("myagency");
		directives.name("custom-name");
		directives.version("2.0");
		
		directives.map(customNameType,SdmxElement.NAME);
		directives.map(customDescriptionType,SdmxElement.DESCRIPTION);
		directives.map(customAnnotationType,SdmxElement.ANNOTATION);
		
		
		Outcome<CodelistBean> outcome = mapper.map(list, directives);
		
		serialiser.serialise(outcome.result(),System.out,Sdmx2XmlDirectives.DEFAULT);	
		
		System.out.println(outcome.report());
		
	}
	
	
}
