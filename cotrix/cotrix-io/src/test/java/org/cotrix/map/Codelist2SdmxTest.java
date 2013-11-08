package org.cotrix.map;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.io.sdmx.Constants.*;
import static org.cotrix.io.sdmx.SdmxElement.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Collection;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.sdmx.SdmxElement;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.cotrix.io.sdmx.serialise.Sdmx2XmlDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.constants.TERTIARY_BOOL;
import org.sdmxsource.sdmx.api.model.beans.base.AnnotationBean;
import org.sdmxsource.sdmx.api.model.beans.base.TextTypeWrapper;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.util.date.DateUtil;

import com.googlecode.jeeunit.JeeunitRunner;

//integration tests

@RunWith(JeeunitRunner.class)
public class Codelist2SdmxTest {

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
	
	@Test 
	public void emptyCodelistWithDefaults() {
		
		Codelist list = codelist().name("list").build();
		
		Outcome<CodelistBean> outcome = mapper.map(list, Codelist2SdmxDirectives.DEFAULT);
		
		System.out.println(outcome.report());
		
		CodelistBean bean = outcome.result();
		
		//default id is codelist name
		assertEquals("list",bean.getId());
		
		//name is also a name in default locale
		assertEquals("list",bean.getName());
		
		//default agency
		assertEquals(DEFAULT_SDMX_AGENCY,bean.getAgencyId());
		
		//inherits list version
		assertEquals(list.version(),bean.getVersion());
		
		//status is undefined
		assertEquals(TERTIARY_BOOL.UNSET,bean.isFinal());
	}
	
	@Test 
	public void emptyCodelistWithCustomisations() {
		
		Codelist list = codelist().name("list").build();
		
		Codelist2SdmxDirectives directives = new Codelist2SdmxDirectives();
		directives.agency("agency");
		directives.version("2");
		directives.isFinal(false);
		directives.name("name");
		
		Outcome<CodelistBean> outcome = mapper.map(list, directives);
		
		System.out.println(outcome.report());
		
		CodelistBean bean = outcome.result();
		
		assertEquals("name",bean.getId());
		
		assertEquals("agency",bean.getAgencyId());
		
		assertEquals("2",bean.getVersion());
		
		assertEquals(TERTIARY_BOOL.FALSE,bean.isFinal());
	}
	
	@Test 
	public void codelistAttributesWithDefaults() {
		
		Attribute a1 = attr().name("a").value("val").build();
		Attribute a2 = attr().name("b").value("val-b").ofType(NAME_TYPE).in("fr").build();
		Attribute a3 = attr().name("a").value("val-c").ofType(ANNOTATION_TYPE).in("sp").build();
		Codelist list = codelist().name("list").attributes(a1,a2,a3).build();
		
		Outcome<CodelistBean> outcome = mapper.map(list, Codelist2SdmxDirectives.DEFAULT);
		
		System.out.println(outcome.report());
		
		CodelistBean bean = outcome.result();
		
		serialise(bean);
		
		assertTrue(contains(bean.getDescriptions(),"val","en"));
		assertTrue(contains(bean.getNames(),"val-b","fr"));
		assertTrue(containsAnnotation(bean.getAnnotations(),"val-c","sp"));
		

	}
	
	@Test 
	public void codeWithDefaults() {
		
		Code code = code().name("c").build();
		Codelist list = codelist().name("list").with(code).build();
		
		Outcome<CodelistBean> outcome = mapper.map(list, Codelist2SdmxDirectives.DEFAULT);
		
		System.out.println(outcome.report());
		
		CodelistBean bean = outcome.result();
		
		serialise(bean);
		
		assertNotNull(bean.getCodeById("c"));
		
	}
	
	private boolean containsAnnotation(Collection<AnnotationBean> list, String value, String language) {
		
		for (AnnotationBean a : list)
			if (contains(a.getText(),value,language))
					return true;
			
		return false;
	}

	private boolean contains(Collection<TextTypeWrapper> list, String value, String language) {
		
		for (TextTypeWrapper w : list)
			if (value.equals(w.getValue()) && language.equals(w.getLocale()))
					return true;
			
		return false;
	}
	
	@Test 
	public void emptyCodeWithDefaults() {
		
		Code code = code().name("code").build();
		Codelist list = codelist().name("list").with(code).build();
		
		Outcome<CodelistBean> outcome = mapper.map(list, Codelist2SdmxDirectives.DEFAULT);
		
		System.out.println(outcome.report());
		
		CodelistBean bean = outcome.result();
		

		serialise(bean);
	}
	
	void serialise(CodelistBean bean) {
		serialiser.serialise(bean,System.out,Sdmx2XmlDirectives.DEFAULT);
	}
}
