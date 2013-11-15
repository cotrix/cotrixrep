package org.cotrix.integration;

import static org.cotrix.domain.dsl.Codes.*;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.io.MapService;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class CloudServiceTests {

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
		
		cloud.publish(bean,new QName("iMarine Registry"));
		
	}
	
	
	@Test
	public void publishToSemanticRepository() {
		
		Code code = code().name("c").build();
		Codelist list = codelist().name("list").with(code).build();
		
		Outcome<CodelistBean> outcome = mapper.map(list, Codelist2SdmxDirectives.DEFAULT);
		
		System.out.println(outcome.report());
		
		CodelistBean bean = outcome.result();
		
		cloud.publish(bean,new QName("semantic-repository"));
		
	}
}
