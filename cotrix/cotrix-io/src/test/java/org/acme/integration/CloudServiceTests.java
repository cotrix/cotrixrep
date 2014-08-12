package org.acme.integration;

import static org.cotrix.domain.dsl.Data.*;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.common.Outcome;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.io.MapService;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class CloudServiceTests extends ApplicationTest {

	@Inject
	CloudService cloud;
	
	@Inject
	MapService mapper;
	
	@Test
	public void publishToGCubeSdmxRegistry() {
		
		Code code = code().name("c").build();
		Codelist list = codelist().name("list").with(code).build();
		
		Outcome<CodelistBean> outcome = mapper.map(list, Codelist2SdmxDirectives.DEFAULT);
		
		System.out.println(outcome.report());
		
		CodelistBean bean = outcome.result();
		
		cloud.publish(list,bean,new QName("iMarine Registry"));
		
	}
	
	
	@Test
	public void publishToSemanticRepository() {
		
		Code code = code().name("c").build();
		Codelist list = codelist().name("list").with(code).build();
		
		Outcome<CodelistBean> outcome = mapper.map(list, Codelist2SdmxDirectives.DEFAULT);
		
		System.out.println(outcome.report());
		
		CodelistBean bean = outcome.result();
		
		cloud.publish(list,bean,new QName("semantic-repository"));
		
	}
}
