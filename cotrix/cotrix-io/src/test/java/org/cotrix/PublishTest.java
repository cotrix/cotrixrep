package org.cotrix;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.io.sdmx.SdmxElement.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.common.Report;
import org.cotrix.domain.Codelist;
import org.cotrix.io.publish.PublicationTask;
import org.cotrix.io.sdmx.Codelist2Sdmx;
import org.cotrix.io.sdmx.SdmxElement;
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
import org.sdmxsource.sdmx.util.date.DateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//integration tests

public class PublishTest {

	//initialise spring underneath sdmxsource to enable proper error reporting and serialisation to XML
	static ApplicationContext context = new ClassPathXmlApplicationContext("sdmxsource-context.xml");
	
	
	
	@Test
	public void codelistCanBeConvertedToSdmx() throws Exception {
		
		QName customNameType = new QName("customname");
		QName customDescriptionType = new QName("customdescription");
		QName customAnnotationType = new QName("customannotation");
		
		Codelist codelist = codelist("1").name("cotrix-testlist").
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
		
		Codelist2Sdmx transform = new Codelist2Sdmx();
		
		SdmxPublishDirectives directives = new SdmxPublishDirectives();
		
		directives.agency("myagency");
		directives.name("custom-name");
		directives.version("2.0");
		
		directives.map(customNameType,SdmxElement.NAME);
		directives.map(customDescriptionType,SdmxElement.DESCRIPTION);
		directives.map(customAnnotationType,SdmxElement.ANNOTATION);
		
		
		CodelistBean bean = transform.apply(codelist, directives);
	
		toXML(bean);
		
		System.out.println(Report.report());
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
