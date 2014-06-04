package org.acme.parse;

import static org.junit.Assert.*;

import java.io.InputStream;

import javax.inject.Inject;

import org.cotrix.io.ParseService;
import org.cotrix.io.sdmx.parse.Stream2SdmxDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class Xml2SdmxTest {

	@Inject
	ParseService parser;
	
	@Test
	public void xml2Sdmx() {
		
		//not much to do beyond test dispatch, parsing is fixed and provided by 3rd party lib
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("samplesdmx.xml");
		
		CodelistBean bean = parser.parse(stream, Stream2SdmxDirectives.DEFAULT);
		
		assertNotNull(bean);
		
	}

}