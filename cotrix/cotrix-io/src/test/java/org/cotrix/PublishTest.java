package org.cotrix;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

import org.cotrix.domain.Codelist;
import org.cotrix.io.publish.PublicationTask;
import org.cotrix.io.sdmx.Codelist2Sdmx;
import org.cotrix.io.sdmx.SdmxPublishDirectives;
import org.cotrix.io.sdmx.SdmxPublishTask;
import org.cotrix.io.utils.Registry;
import org.junit.Test;
import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.structureparser.manager.impl.StructureWritingManagerImpl;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//integration tests

public class PublishTest {

	//initialise spring underneath sdmxsource to enable proper error reporting and serialisation to XML
	static ApplicationContext context = new ClassPathXmlApplicationContext("sdmxsource-context.xml");
	
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
	
	@Test
	public void codelistCanBeConvertedToSdmx() throws Exception {
		
		
		Codelist2Sdmx transform = new Codelist2Sdmx();
		CodelistBean bean = transform.apply(codelist);
		toXML(bean);
	}
	
	
	@Test
	public void defaultDirectivesAreDispatched() throws Exception {
		
		SdmxPublishTask task =  new SdmxPublishTask(null);
		Collection<? extends PublicationTask<?>> tasks = asList(task);
		Registry<PublicationTask<?>> registry = new Registry<PublicationTask<?>>(tasks);
		
		assertEquals(task,registry.get(SdmxPublishDirectives.DEFAULT));
	}
	
	
	
	void toXML(CodelistBean list) {

		SdmxBeans beans = new SdmxBeansImpl(list);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		
		STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
		
		StructureWritingManager manager = new StructureWritingManagerImpl();
		manager.writeStructures(beans,format, stream);
		
		System.out.println(stream.toString());
	}
}
