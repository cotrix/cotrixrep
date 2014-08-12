package org.acme.map;

import static org.cotrix.domain.dsl.Entities.*;
import static org.cotrix.io.sdmx.Constants.*;
import static org.cotrix.io.sdmx.SdmxElement.*;
import static org.junit.Assert.*;
import static org.sdmxsource.sdmx.util.date.DateUtil.*;

import java.util.Calendar;
import java.util.Collection;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.io.MapService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.cotrix.io.sdmx.serialise.Sdmx2XmlDirectives;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.constants.TERTIARY_BOOL;
import org.sdmxsource.sdmx.api.model.beans.base.AnnotationBean;
import org.sdmxsource.sdmx.api.model.beans.base.TextTypeWrapper;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodeBean;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

import com.googlecode.jeeunit.JeeunitRunner;

//integration tests

@RunWith(JeeunitRunner.class)
public class Codelist2SdmxTest {

	@Inject
	MapService mapper;
	
	@Inject
	SerialisationService serialiser;

	
	//---- codelist fixture
	
	Attribute from = attribute().name("from").value(formatDate(Calendar.getInstance().getTime())).build();
	Attribute not_a_date = attribute().name("to").value("bad").build();
	
	Code ttc = code().name("ttc").build();
	Codelist ttlist = codelist().name("tt").with(ttc).build();

	LinkDefinition nnl = linkdef().name("nnl").target(ttlist).build();
	
	Link ll1 = link().instanceOf(nnl).target(ttc).build();
	Attribute ta = attribute().name("ta").value("tv").build();
	Code tc = code().name("tc").attributes(ta).links(ll1).build();
	Codelist tlist = codelist().name("t").links(nnl).with(tc).build();

	LinkDefinition nl = linkdef().name("nl").target(tlist).build();
	LinkDefinition al = linkdef().name("al").target(tlist).anchorTo(ta).build();
	LinkDefinition ll = linkdef().name("ll").target(tlist).anchorTo(nnl).build();

	Link l1 = link().instanceOf(nl).target(tc).build();
	Link l2 = link().instanceOf(al).target(tc).build();
	Link l3 = link().instanceOf(ll).target(tc).build();
	
	Attribute a1 = attribute().name("a1").value("v1").build();
	Attribute a2 = attribute().name("a2").value("v2").in("es").build();
	Attribute a3 = attribute().name("a3").value("v3").in("en").build();
	
	Code c1 = code().name("c1").links(l1).build();
	Code c2 = code().name("c2").attributes(a1,a2,from,not_a_date).links(l2).build();
	Code c3 = code().name("c3").links(l3).build();
	
	Codelist list = codelist().name("cotrix-testlist").with(c1,c2,c3)
						
						.links(nl,al,ll)
						.attributes(a1,a2,from,not_a_date)
						.version("1.0")
						.build();

	Codelist2SdmxDirectives directives = new Codelist2SdmxDirectives();

	@Test 
	public void codelistDefaults() {
		
		CodelistBean bean = mapper.map(list,directives).result();
		
		assertEquals(list.qname().getLocalPart(),bean.getId());
		assertEquals(list.qname().getLocalPart(),bean.getName());
		assertEquals(DEFAULT_SDMX_AGENCY,bean.getAgencyId());
		assertEquals(list.version(),bean.getVersion());
		assertEquals(TERTIARY_BOOL.UNSET,bean.isFinal());
	}
	
	@Test 
	public void codeDefaults() {
		
		CodelistBean bean = mapper.map(list,directives).result();
		
		CodeBean codebean = bean.getCodeById(c1.qname().getLocalPart());
		
		assertNotNull(codebean);
		
		assertEquals(c1.qname().getLocalPart(),codebean.getName());
	}

	
	@Test 
	public void codelistGlobals() {

		directives.agency("a").id("n").version("2").isFinal(true);
		
		CodelistBean bean = mapper.map(list, directives).result();

		assertEquals("a",bean.getAgencyId());
		assertEquals("n",bean.getId());
		assertEquals("2",bean.getVersion());
		assertTrue(bean.isFinal().isTrue());

	}
	
	
	@Test 
	public void codelistNames() {
		
		directives.map(a1).to(NAME).forCodelist()
			      .map(a2).to(NAME).forCodelist();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		assertTrue(contains(bean.getNames(),a1.value(),"en"));
		assertTrue(contains(bean.getNames(),a2.value(),a2.language()));
		
	}
	
	@Test 
	public void codelistNamesWithSameLangCheck() {
		
		directives.map(a1).to(NAME).forCodelist()
			      .map(a3).to(NAME).forCodelist();
		
		System.out.println(directives.errors());

		assertFalse(directives.errors().isEmpty());
		
	}
	
	@Test 
	public void codelistWithoutNamesCheck() {
		
		assertFalse(directives.errors().isEmpty());
		
	}
	
	@Test 
	public void codeNames() {
		
		directives.map(a1).to(NAME).forCodes()
			      .map(a2).to(NAME).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c2.qname().getLocalPart());
		
		assertTrue(contains(codebean.getNames(),a1.value(),"en"));
		assertTrue(contains(codebean.getNames(),a2.value(),a2.language()));
		
	}
	
	@Test 
	public void codeNameViaNameLink() {
		
		directives.map(l1).to(NAME).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c1.qname().getLocalPart());
		
		assertTrue(contains(codebean.getNames(),tc.qname().getLocalPart(),"en"));
		
	}
	
	@Test 
	public void codeDescriptionViaNameLink() {
		
		directives.map(l1).to(DESCRIPTION).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c1.qname().getLocalPart());
		
		assertTrue(contains(codebean.getDescriptions(),tc.qname().getLocalPart(),"en"));
		
	}
	
	@Test 
	public void codeAnnotationViaNameLink() {
		
		directives.map(l1).to(ANNOTATION).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c1.qname().getLocalPart());
		
		assertTrue(containsAnnotation(codebean.getAnnotations(),tc.qname().getLocalPart(),"en"));
		
	}
	
	@Test 
	public void codeNameViaAtttributeLink() {
		
		directives.map(l2).to(NAME).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c2.qname().getLocalPart());
		
		assertTrue(contains(codebean.getNames(),ta.value(),"en"));
		
	}
	
	@Test 
	public void codeDescriptionViaAtttributeLink() {
		
		directives.map(l2).to(DESCRIPTION).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c2.qname().getLocalPart());
		
		assertTrue(contains(codebean.getDescriptions(),ta.value(),"en"));
		
	}
	
	@Test 
	public void codeAnnotationViaAtttributeLink() {
		
		directives.map(l2).to(ANNOTATION).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c2.qname().getLocalPart());
		
		assertTrue(containsAnnotation(codebean.getAnnotations(),ta.value(),"en"));
		
	}
	
	@Test 
	public void codeNameViaLinkofLink() {
		
		directives.map(l3).to(NAME).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c3.qname().getLocalPart());
		
		assertTrue(contains(codebean.getNames(),ttc.qname().getLocalPart(),"en"));
		
	}
	
	@Test 
	public void codeDescriptionViaLinkofLink() {
		
		directives.map(l3).to(DESCRIPTION).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c3.qname().getLocalPart());
		
		assertTrue(contains(codebean.getDescriptions(),ttc.qname().getLocalPart(),"en"));
		
	}

	@Test 
	public void codeAnnotationViaLinkofLink() {
		
		directives.map(l3).to(ANNOTATION).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c3.qname().getLocalPart());
		
		assertTrue(containsAnnotation(codebean.getAnnotations(),ttc.qname().getLocalPart(),"en"));
		
	}

	@Test 
	public void codelistDescriptions() {
		
		directives.map(a1).to(DESCRIPTION).forCodelist()
				  .map(a2).to(DESCRIPTION).forCodelist();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		assertTrue(contains(bean.getDescriptions(),a1.value(),"en"));
		assertTrue(contains(bean.getDescriptions(),a2.value(),a2.language()));
		
	}
	
	@Test 
	public void codeDescriptions() {
		
		directives.map(a1).to(DESCRIPTION).forCodes()
				  .map(a2).to(DESCRIPTION).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c2.qname().getLocalPart());
		
		assertTrue(contains(codebean.getDescriptions(),a1.value(),"en"));
		assertTrue(contains(codebean.getDescriptions(),a2.value(),a2.language()));
		
	}
	
	@Test 
	public void codelistAnnotations() {
		
		directives.map(a1).to(ANNOTATION).forCodelist()
		          .map(a2).to(ANNOTATION).forCodelist();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		assertTrue(containsAnnotation(bean.getAnnotations(),a1.value(),"en"));
		assertTrue(containsAnnotation(bean.getAnnotations(),a2.value(),a2.language()));
		
	}
	
	@Test 
	public void codeAnnotations() {
		
		directives.map(a1).to(ANNOTATION).forCodes()
		          .map(a2).to(ANNOTATION).forCodes();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		CodeBean codebean = bean.getCodeById(c2.qname().getLocalPart());
		
		assertTrue(containsAnnotation(codebean.getAnnotations(),a1.value(),"en"));
		assertTrue(containsAnnotation(codebean.getAnnotations(),a2.value(),a2.language()));
		
	}
	
	@Test 
	public void codelistValidity() throws Exception {
		
		directives.map(from).to(VALID_FROM).forCodelist()
		          .map(not_a_date).to(VALID_TO).forCodelist();
		
		CodelistBean bean = mapper.map(list, directives).result();
		
		assertNotNull(bean.getStartDate());
		assertNull(bean.getEndDate());
	}
	
	
	
	@Ignore @Test //enable interactively 
	public void codelist2Sdmx2Xml() {
		
		directives.agency("a").id("n").version("2").isFinal(true)
				  .map(a1).to(NAME).forCodelist()
				  .map(a2).to(DESCRIPTION).forCodelist()
				  
				  .map(a1).to(NAME).forCodes()
				  .map(a2).to(DESCRIPTION).forCodes();
				  
		
		Outcome<CodelistBean> outcome = mapper.map(list, directives);
		
		serialiser.serialise(outcome.result(),System.out,Sdmx2XmlDirectives.DEFAULT);	
		
		System.out.println(outcome.report());
		
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
