package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.io.PublicationService;
import org.cotrix.io.sdmx.SdmxPublishDirectives;
import org.junit.Ignore;
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
	
	@Ignore
	@Test
	public void importASFIS() throws Exception {

//		File file = new File("src/test/resources/ASFIS_sp_Feb_2012.txt");
//		
//		List<String> types = new ArrayList<String>();
//		types.add("ISSCAAP");
//		types.add("TAXOCODE");
//		types.add("3A_CODE");
//		types.add("Scientific_name");
//		types.add("English_name");
//		types.add("French_name");
//		types.add("Spanish_name");
//		types.add("Author");
//		types.add("Family");
//		types.add("Order");
//		types.add("Stats_data");
//
//		
//		CSVOptions options = new CSVOptions();
//		options.setDelimiter('\t');
//		options.setColumns(types, true);
//
//		CodelistMapping mapping = new CodelistMapping("3A_CODE");
//		QName asfisName = new QName("asfis-2012");
//		mapping.setName(asfisName);
//
//		List<AttributeMapping> attrs = new ArrayList<AttributeMapping>();
//		for (String type : types) {
//			AttributeMapping attr = new AttributeMapping(type.trim());
//			attrs.add(attr);
//		}
//		mapping.setAttributeMappings(attrs);
//
//		CSV2Codelist directives = new CSV2Codelist(mapping, options);
//		
//		
//		Outcome<Codelist> outcome = service.importCodelist(new FileInputStream(file),directives);
//		
//		System.out.println(outcome.report());
//		
//		Codelist asfis = outcome.result();
//		
//		Codelist2Sdmx transform = new Codelist2Sdmx();
//		transform.apply(asfis);
//		
//		//System.out.println(asfis.codes());
//		assertEquals(asfisName, asfis.name());
		
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