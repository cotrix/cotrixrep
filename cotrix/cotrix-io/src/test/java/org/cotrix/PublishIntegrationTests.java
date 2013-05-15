package org.cotrix;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.io.PublicationService;
import org.cotrix.io.sdmx.SdmxPublishDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtual.sdmxregistry.GCubeProxy;
import org.virtual.sdmxregistry.GCubeRegistry;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class PublishIntegrationTests {

	Codelist codelist = codelist("1").name("cotrix-testlist").
			with(
				code("1").name("code1").build()
				,code("2").name("code2").attributes(
						attr().name("attr1").value("value1").build()
					   , attr().name("attr2").value("value2").in("fr").build()
					   ,attr().name("attr3").value("value3").ofType(NAME.toString()).build()
						,attr().name("attr4").value("value4").ofType(NAME.toString()).in("sp").build()
			).build())
			.version("1.0").build();
	
	
	
	@Inject
	PublicationService publisher;
	
	@Inject
	VirtualRepository repository;
	
	@Test
	public void servicesAreInjected() throws Exception {
			
		assertNotNull(publisher);
		
	}
	
	@Test
	public void publishedInSdmxWithDefaultDirectives() throws Exception {
		
		GCubeRegistry registry = new GCubeRegistry(new QName("test"), "/gcube/devsec");
		GCubeProxy proxy = new GCubeProxy(registry);
		RepositoryService testService = new RepositoryService(registry.name(), proxy);
		
		repository.services().add(testService);
		
		publisher.publish(codelist,SdmxPublishDirectives.DEFAULT,registry.name());
	}
}